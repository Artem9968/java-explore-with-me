package ru.practicum.mainservice.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.mapper.request.ParticipationRequestMapper;
import ru.practicum.mainservice.model.request.ParticipationRequest;
import ru.practicum.mainservice.service.request.ParticipationRequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class ParticipationRequestController {

    private final ParticipationRequestService participationRequestService;

    @GetMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable int userId,
                                                             @PathVariable int eventId) {
        log.info("Пользователь id={} проверяет заявки на событие id={}", userId, eventId);
        return participationRequestService.findEventRequests(userId, eventId)
                .stream()
                .map(ParticipationRequestMapper::toRequestDto)
                .toList();
    }

    @PatchMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestsForEvent(@PathVariable int userId,
                                                                 @PathVariable int eventId,
                                                                 @RequestBody EventRequestStatusUpdateRequest requestUpdates) {
        log.info("Пользователь id={} обновляет статусы заявок для события id={}", userId, eventId);
        return participationRequestService.updateRequestStatuses(userId, eventId, requestUpdates);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable int userId,
                                                 @RequestParam Integer eventId) {
        log.info("Пользователь id={} создает заявку на участие в событии id={}", userId, eventId);
        ParticipationRequest participationRequest = participationRequestService.createRequest(userId, eventId);
        return ParticipationRequestMapper.toRequestDto(participationRequest);
    }

    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserRequests(@PathVariable int userId) {
        log.info("Пользователь id={} получает свои заявки", userId);
        return participationRequestService.findUserRequests(userId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable int userId,
                                                 @PathVariable int requestId) {
        log.info("Пользователь id={} отменяет заявку id={}", userId, requestId);
        return ParticipationRequestMapper.toRequestDto(participationRequestService.cancelRequest(userId, requestId));
    }
}

