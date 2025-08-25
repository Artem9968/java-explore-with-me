package ru.practicum.mainservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.EventFullDto;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.NewEventDto;
import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.dto.RequestGroupDto;
import ru.practicum.mainservice.dto.RequestUpdateDto;
import ru.practicum.mainservice.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.Request;
import ru.practicum.mainservice.service.EventService;
import ru.practicum.mainservice.service.RequestService;


import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/{id}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable int id,
                                    @Validated @RequestBody NewEventDto eventDto) {
        log.info("Пользователь id={} cоздает новое событие: {}", id, eventDto.toString());
        return eventService.createEvent(eventDto, id);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable int userId,
                                 @PathVariable int eventId) {

        log.info("Пользователь id={} запрашивает информацию о событии id={}. ",
                userId, eventId);
        return eventService.getEventById(eventId, userId);
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable int userId,
                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Пользователь id={} запрашивает информацию об инциированных событиях.", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @Validated @RequestBody UpdateEventUserRequest eventDto) {
        log.info("Пользователь id={} изменяет информацию об инициированном событии. {}", userId, eventDto.toString());
        return eventService.patchEvent(eventId, eventDto, userId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> findRequestsByEventId(@PathVariable int userId,
                                                  @PathVariable int eventId) {
        log.info("Пользователь id={} выполняет поиск запросов на участие в событии id={}.",
                userId, eventId);
        return requestService.getRequestsByEventId(userId, eventId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .toList();
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestGroupDto patchRequestsByEventId(@PathVariable int userId,
                                                  @PathVariable int eventId,
                                                  @RequestBody RequestUpdateDto requestUpdateDto) {
        log.info("Пользователь id={} модерирует запросы на событие id={}.",
                userId, eventId);
        return requestService.updateRequestsStatus(userId, eventId, requestUpdateDto);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Integer userId,
                                    @RequestParam(name = "eventId") Integer eventId) {
        log.info("Пользователь id={} создает запрос на участие в событии id={}.",
                userId, eventId);
        Request request = requestService.createRequest(userId, eventId);
        return RequestMapper.toRequestDto(request);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> findRequestsByUserId(@PathVariable Integer userId) {
        log.info("Пользователь id={} выполняет поиск собственных заявок.", userId);
        return requestService.getRequestsByUserId(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto canceledRequestById(@PathVariable Integer userId,
                                          @PathVariable Integer requestId) {
        log.info("Пользователь id={} отменяет запрос id={}.", userId, requestId);
        return RequestMapper.toRequestDto(requestService.canceledRequest(userId, requestId));
    }
}
