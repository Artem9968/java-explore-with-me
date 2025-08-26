package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.dto.RequestGroupDto;
import ru.practicum.mainservice.dto.RequestUpdateDto;
import ru.practicum.mainservice.exception.DataConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidationException;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.Request;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.enums.RequestStatus;
import ru.practicum.mainservice.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findUserRequests(Integer userId) {
        List<Request> requests = requestRepository.findAllByRequester_Id(userId);
        return requests.isEmpty()
                ? List.of()
                : requests.stream().map(RequestMapper::toRequestDto).toList();
    }

    @Override
    public Request cancelRequest(Integer userId, Integer requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявка отсутствует"));

        if (!request.getRequester().getId().equals(userId)) {
            throw new ValidationException(
                    "Ошибка: пользователь id=" + userId +
                            " не может отменить чужую заявку (принадлежит id=" + request.getRequester().getId() + ")"
            );
        }

        request.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);
    }

    @Override
    public Request createRequest(Integer userId, Integer eventId) {
        Event event = eventService.findEventById(eventId);
        validateCreateRequest(userId, event);

        User user = userService.findUserById(userId);

        Request request = new Request();
        request.setRequester(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return requestRepository.save(request);
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
    public List<Request> findEventRequests(Integer userId, Integer eventId) {
        Event event = eventService.findEventById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(
                    "Ошибка: только инициатор может просматривать заявки на участие. " +
                            "Попытка от пользователя id=" + userId +
                            ", настоящий инициатор: " + event.getInitiator().getId()
            );
        }
        return requestRepository.findAllByEvent_Id(eventId);
    }

    @Override
    public RequestGroupDto updateRequestStatuses(Integer userId, Integer eventId, RequestUpdateDto requestUpdateDto) {
        Event event = eventService.findEventById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(
                    "Ошибка: пользователь id=" + userId +
                            " не является инициатором события id=" + eventId
            );
        }

        Integer confirmed = requestRepository.countConfirmedByEventId(eventId);
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit().equals(confirmed)) {
            throw new DataConflictException(
                    "Ошибка: для события id=" + eventId +
                            " больше нельзя подтвердить заявки — лимит уже исчерпан"
            );
        }

        List<Integer> requestIds = requestUpdateDto.getRequestIds();
        if (requestIds.isEmpty()) {
            return new RequestGroupDto();
        }

        Collections.sort(requestIds);
        return applyStatuses(requestIds, requestUpdateDto.getStatus(), confirmed, event);
    }

    private RequestGroupDto applyStatuses(List<Integer> requestIds,
                                          RequestStatus newStatus,
                                          Integer confirmed,
                                          Event event) {
        RequestGroupDto result = new RequestGroupDto();

        for (Integer reqId : requestIds) {
            Request request = requestRepository.findById(reqId)
                    .orElseThrow(() -> new NotFoundException("Заявка  не найдена"));

            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new DataConflictException(
                        "Ошибка: изменить можно только заявки в ожидании. " +
                                "id=" + reqId + ", текущий статус: " + request.getStatus()
                );
            }

            RequestStatus finalStatus = newStatus;
            if (confirmed.equals(event.getParticipantLimit())) {
                finalStatus = RequestStatus.REJECTED;
            }

            request.setStatus(finalStatus);
            Request saved = requestRepository.save(request);

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

