package ru.practicum.mainservice.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.event.UpdateEventUserRequest;
import ru.practicum.mainservice.service.request.ParticipationRequestService;
import ru.practicum.mainservice.service.event.EventService;
import ru.practicum.mainservice.model.request.ParticipationRequest;
import ru.practicum.mainservice.mapper.request.ParticipationRequestMapper;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable int userId,
                                    @Validated @RequestBody NewEventDto newEventData) {
        log.info("Пользователь id={} создает новое событие: {}", userId, newEventData);
        return eventService.createEvent(newEventData, userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable int userId,
                                 @PathVariable int eventId) {
        log.info("Пользователь id={} запрашивает событие id={}", userId, eventId);
        return eventService.findUserEventById(eventId, userId);
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable int userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("Пользователь id={} получает список своих событий.", userId);
        return eventService.findUserEvents(userId, from, size);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable int userId,
                                    @PathVariable int eventId,
                                    @Validated @RequestBody UpdateEventUserRequest updateData) {
        log.info("Пользователь id={} обновляет событие id={}: {}", userId, eventId, updateData);
        return eventService.updateEventByUser(eventId, updateData, userId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable int userId,
                                                             @PathVariable int eventId) {
        log.info("Пользователь id={} проверяет заявки на событие id={}", userId, eventId);
        return participationRequestService.findEventRequests(userId, eventId)
                .stream()
                .map(ParticipationRequestMapper::toRequestDto)
                .toList();
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestsForEvent(@PathVariable int userId,
                                                                 @PathVariable int eventId,
                                                                 @RequestBody EventRequestStatusUpdateRequest requestUpdates) {
        log.info("Пользователь id={} обновляет статусы заявок для события id={}", userId, eventId);
        return participationRequestService.updateRequestStatuses(userId, eventId, requestUpdates);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable int userId,
                                                 @RequestParam Integer eventId) {
        log.info("Пользователь id={} создает заявку на участие в событии id={}", userId, eventId);
        ParticipationRequest participationRequest = participationRequestService.createRequest(userId, eventId);
        return ParticipationRequestMapper.toRequestDto(participationRequest);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserRequests(@PathVariable int userId) {
        log.info("Пользователь id={} получает свои заявки", userId);
        return participationRequestService.findUserRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable int userId,
                                                 @PathVariable int requestId) {
        log.info("Пользователь id={} отменяет заявку id={}", userId, requestId);
        return ParticipationRequestMapper.toRequestDto(participationRequestService.cancelRequest(userId, requestId));
    }
}

