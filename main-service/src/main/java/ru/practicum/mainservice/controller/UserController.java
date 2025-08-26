package ru.practicum.mainservice.controller;

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


import ru.practicum.mainservice.dto.EventFullDto;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.NewEventDto;
import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.dto.RequestGroupDto;
import ru.practicum.mainservice.dto.RequestUpdateDto;
import ru.practicum.mainservice.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.service.RequestService;
import ru.practicum.mainservice.service.EventService;
import ru.practicum.mainservice.model.Request;
import ru.practicum.mainservice.mapper.RequestMapper;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final EventService eventService;
    private final RequestService requestService;

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
    public List<RequestDto> getRequestsForEvent(@PathVariable int userId,
                                                @PathVariable int eventId) {
        log.info("Пользователь id={} проверяет заявки на событие id={}", userId, eventId);
        return requestService.findEventRequests(userId, eventId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .toList();
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestGroupDto updateRequestsForEvent(@PathVariable int userId,
                                                  @PathVariable int eventId,
                                                  @RequestBody RequestUpdateDto requestUpdates) {
        log.info("Пользователь id={} обновляет статусы заявок для события id={}", userId, eventId);
        return requestService.updateRequestStatuses(userId, eventId, requestUpdates);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable int userId,
                                    @RequestParam Integer eventId) {
        log.info("Пользователь id={} создает заявку на участие в событии id={}", userId, eventId);
        Request request = requestService.createRequest(userId, eventId);
        return RequestMapper.toRequestDto(request);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getUserRequests(@PathVariable int userId) {
        log.info("Пользователь id={} получает свои заявки", userId);
        return requestService.findUserRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable int userId,
                                    @PathVariable int requestId) {
        log.info("Пользователь id={} отменяет заявку id={}", userId, requestId);
        return RequestMapper.toRequestDto(requestService.cancelRequest(userId, requestId));
    }
}

