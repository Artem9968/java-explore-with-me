package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.dto.RequestGroupDto;
import ru.practicum.mainservice.dto.RequestUpdateDto;
import ru.practicum.mainservice.model.enums.EventStatus;
import ru.practicum.mainservice.model.enums.RequestState;
import ru.practicum.mainservice.exception.DataConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidationException;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.EventRequest;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public EventRequest createRequest(Integer userId, Integer eventId) {
        Event event = eventService.findEventById(eventId);
        if (event.getOrganizer().getId().equals(userId)) {
            throw new DataConflictException(
                    "Field: event.initiator_id. Error: " +
                            "Инициатор события не может добавить запрос на участие в своём событии. " +
                            "Value: " + event.getOrganizer().getId()
            );
        }
        if (!event.getState().equals(EventStatus.ACTIVE)) {
            throw new DataConflictException(
                    "Field: event.state. Error: " +
                            "Нельзя участвовать в неопубликованном событии. " +
                            "Value: " + event.getState()
            );
        }
        Integer confirmedRequests = event.getApprovedParticipants();
        if (confirmedRequests != null && event.getMaxAttendees() > 0) {
            if (confirmedRequests.equals(event.getMaxAttendees())) {
                throw new DataConflictException(
                        "Field: event.state. Error: " +
                                "У события достигнут лимит запросов на участие. " +
                                "Value: " + confirmedRequests
                );
            }
        }
        EventRequest eventRequest = new EventRequest();
        User user = userService.getUserById(userId);
        eventRequest.setRequestingUser(user);
        eventRequest.setEvent(event);
        eventRequest.setRequestState(RequestState.IN_REVIEW);
        if (!event.getRequiresApproval() || event.getMaxAttendees() == 0) {
            eventRequest.setRequestState(RequestState.APPROVED);
        }
        eventRequest.setRequestDate(LocalDateTime.now());
        return requestRepository.save(eventRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByUserId(Integer userId) {
        List<EventRequest> eventRequests = requestRepository.findAllByRequester_Id(userId);
        if (eventRequests.isEmpty()) {
            return List.of();
        }
        return eventRequests.stream().map(RequestMapper::toRequestDto).toList();
    }

    @Override
    public EventRequest canceledRequest(Integer userId, Integer requestId) {
        EventRequest eventRequest = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException("Не найден запрос id=" + requestId));
        if (!eventRequest.getRequestingUser().getId().equals(userId)) {
            throw new ValidationException(
                    "Field: request.requester.id. Error: " +
                            "Нельзя отменить чужой запрос. " +
                            "Value: " + eventRequest.getRequestingUser().getId()
            );
        }
        eventRequest.setRequestState(RequestState.CANCELLED);
        return requestRepository.save(eventRequest);
    }

    /**
     * Метод изменения поиска запросов к событию
     */
    @Override
    public List<EventRequest> getRequestsByEventId(Integer userId, Integer eventId) {
        Event event = eventService.findEventById(eventId);
        if (!event.getOrganizer().getId().equals(userId)) {
            throw new ValidationException(
                    "Field: event.initiator_id. " +
                            "Error: пользователь id=" + userId + " не является инициатором события id=" + eventId +
                            ". Value: " + event.getOrganizer().getId()
            );
        }
        return requestRepository.findAllByEvent_Id(eventId);
    }

    /**
     * Метод изменения статуса запросов
     */
    @Override
    public RequestGroupDto updateRequestsStatus(Integer userId, Integer eventId, RequestUpdateDto requestUpdateDto) {
        Event event = eventService.findEventById(eventId);
        if (!event.getOrganizer().getId().equals(userId)) {
            throw new ValidationException(
                    "Field: event.initiator_id. Error: " +
                            "Пользователь id=" + userId + " не является инициатором события id=" + eventId +
                            ". Value: " + event.getOrganizer().getId()
            );
        }

        // ...нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
        // (Ожидается код ошибки 409)
        Integer confirmedRequests = requestRepository.getCountConfirmedRequestsByEventId(eventId);
        if ((event.getMaxAttendees() > 0)
                && event.getMaxAttendees().equals(confirmedRequests)) {
            throw new DataConflictException(
                    "Field: event.confirmedRequests. " +
                            "Error: Достигнуто максимальное количество заявок для события id=" + eventId +
                            ". Value: " + confirmedRequests
            );
        }

        RequestGroupDto requestGroupDto = new RequestGroupDto();
        List<Integer> requestIds = requestUpdateDto.getRequestIds();
        if (requestIds.isEmpty()) {
            return requestGroupDto;
        }
        Collections.sort(requestIds);
        RequestState status = requestUpdateDto.getStatus();

        // Проверяем заявки из списка
        for (Integer requestId : requestIds) {
            EventRequest eventRequest = requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Не найдена заявка id=" + requestId));
            // ... статус можно изменить только у заявок, находящихся в состоянии ожидания
            // (Ожидается код ошибки 409)
            if (!eventRequest.getRequestState().equals(RequestState.IN_REVIEW)) {
                throw new DataConflictException(
                        "Field: request.status. " +
                                "Error: недопустимый статус заявки id=" + requestId +
                                ". Value: " + eventRequest.getRequestState()
                );
            }
            // ... если при подтверждении данной заявки, лимит заявок для события исчерпан,
            // то все неподтверждённые заявки необходимо отклонить
            if (confirmedRequests.equals(event.getMaxAttendees())) {
                status = RequestState.DECLINED;
            }
            eventRequest.setRequestState(status);
            EventRequest savedEventRequest = requestRepository.save(eventRequest);
            if (savedEventRequest.getRequestState().equals(RequestState.APPROVED)) {
                requestGroupDto.getConfirmedRequests().add(RequestMapper.toRequestDto(savedEventRequest));
                confirmedRequests++;
            } else if (savedEventRequest.getRequestState().equals(RequestState.DECLINED)) {
                requestGroupDto.getRejectedRequests().add(RequestMapper.toRequestDto(savedEventRequest));
            }
        }
        return requestGroupDto;
    }
}
