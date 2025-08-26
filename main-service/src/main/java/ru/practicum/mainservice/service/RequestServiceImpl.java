package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.exception.DataConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidationException;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.request.ParticipationRequest;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.enums.RequestStatus;
import ru.practicum.mainservice.repository.request.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findUserRequests(Integer userId) {
        List<ParticipationRequest> participationRequests = participationRequestRepository.findAllByRequester_Id(userId);
        return participationRequests.isEmpty()
                ? List.of()
                : participationRequests.stream().map(RequestMapper::toRequestDto).toList();
    }

    @Override
    public ParticipationRequest cancelRequest(Integer userId, Integer requestId) {
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявка отсутствует"));

        if (!participationRequest.getRequester().getId().equals(userId)) {
            throw new ValidationException(
                    "Ошибка: пользователь id=" + userId +
                            " не может отменить чужую заявку (принадлежит id=" + participationRequest.getRequester().getId() + ")"
            );
        }

        participationRequest.setStatus(RequestStatus.CANCELED);
        return participationRequestRepository.save(participationRequest);
    }

    @Override
    public ParticipationRequest createRequest(Integer userId, Integer eventId) {
        Event event = eventService.findEventById(eventId);
        validateCreateRequest(userId, event);

        User user = userService.findUserById(userId);

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(user);
        participationRequest.setEvent(event);
        participationRequest.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
        } else {
            participationRequest.setStatus(RequestStatus.PENDING);
        }
        return participationRequestRepository.save(participationRequest);
    }

    private void validateCreateRequest(Integer userId, Event event) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new DataConflictException(
                    "Ошибка: инициатор события не может подавать заявку на участие в своём событии. " +
                            "Идентификатор инициатора: " + event.getInitiator().getId()
            );
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new DataConflictException(
                    "Ошибка: участие возможно только в опубликованном событии. " +
                            "Текущее состояние: " + event.getState()
            );
        }
        Integer confirmed = event.getConfirmedRequests();
        if (confirmed != null && event.getParticipantLimit() > 0
                && confirmed.equals(event.getParticipantLimit())) {
            throw new DataConflictException(
                    "Ошибка: лимит участников для события достигнут. " +
                            "Подтверждено: " + confirmed
            );
        }
    }

    @Override
    public List<ParticipationRequest> findEventRequests(Integer userId, Integer eventId) {
        Event event = eventService.findEventById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(
                    "Ошибка: только инициатор может просматривать заявки на участие. " +
                            "Попытка от пользователя id=" + userId +
                            ", настоящий инициатор: " + event.getInitiator().getId()
            );
        }
        return participationRequestRepository.findAllByEvent_Id(eventId);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatuses(Integer userId, Integer eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = eventService.findEventById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(
                    "Ошибка: пользователь id=" + userId +
                            " не является инициатором события id=" + eventId
            );
        }

        Integer confirmed = participationRequestRepository.countConfirmedByEventId(eventId);
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit().equals(confirmed)) {
            throw new DataConflictException(
                    "Ошибка: для события id=" + eventId +
                            " больше нельзя подтвердить заявки — лимит уже исчерпан"
            );
        }

        List<Integer> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        if (requestIds.isEmpty()) {
            return new EventRequestStatusUpdateResult();
        }

        Collections.sort(requestIds);
        return applyStatuses(requestIds, eventRequestStatusUpdateRequest.getStatus(), confirmed, event);
    }

    private EventRequestStatusUpdateResult applyStatuses(List<Integer> requestIds,
                                                         RequestStatus newStatus,
                                                         Integer confirmed,
                                                         Event event) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        for (Integer reqId : requestIds) {
            ParticipationRequest participationRequest = participationRequestRepository.findById(reqId)
                    .orElseThrow(() -> new NotFoundException("Заявка  не найдена"));

            if (!participationRequest.getStatus().equals(RequestStatus.PENDING)) {
                throw new DataConflictException(
                        "Ошибка: изменить можно только заявки в ожидании. " +
                                "id=" + reqId + ", текущий статус: " + participationRequest.getStatus()
                );
            }

            RequestStatus finalStatus = newStatus;
            if (confirmed.equals(event.getParticipantLimit())) {
                finalStatus = RequestStatus.REJECTED;
            }

            participationRequest.setStatus(finalStatus);
            ParticipationRequest saved = participationRequestRepository.save(participationRequest);

            if (saved.getStatus().equals(RequestStatus.CONFIRMED)) {
                result.getConfirmedRequests().add(RequestMapper.toRequestDto(saved));
                confirmed++;
            } else if (saved.getStatus().equals(RequestStatus.REJECTED)) {
                result.getRejectedRequests().add(RequestMapper.toRequestDto(saved));
            }
        }
        return result;
    }
}

