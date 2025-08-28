package ru.practicum.mainservice.controller.pub;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.event.EventMapper;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.service.event.EventService;
import ru.practicum.statsclient.StatsClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class PublicEventController {
    private final StatsClient statsClient;
    private final EventService eventService;

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEventById(@PathVariable("id") int id, HttpServletRequest request) {
        log.info("Запрос события по id={}", id);
        statsClient.hitInfo(applicationName, request.getRequestURI(), request.getRemoteAddr());
        log.info("Хит отправлен в stats-service");

        Event event = eventService.findEventById(id);
        log.info("Событие загружено: id={}, название='{}', статус={}, просмотры={}",
                event.getId(), event.getTitle(), event.getState(), event.getCachedViews());

        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.error("Событие не опубликовано! id={}, статус={}", event.getId(), event.getState());
            throw new NotFoundException("Среди опубликованных не найдено событие id=" + id);
        }

        EventFullDto dto = EventMapper.toFullDto(event);
        log.info("Возврат EventFullDto, просмотры={}", dto.getViews());

        return dto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findAllEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Integer> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request) {

        log.info("Пользователь запрашивает события: текст='{}', категории={}, начало диапазона={}, конец диапазона={}",
                text, categories, rangeStart, rangeEnd);

        statsClient.hitInfo(applicationName, "/events", request.getRemoteAddr());
        log.info("Хит отправлен в сервис статистики для эндпоинта /events");

        return eventService.findEventsByParameters(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
    }
}


