package ru.practicum.mainservice.controller.admin;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.UpdateEventAdminRequest;
import ru.practicum.mainservice.service.event.EventService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEvents(
            @RequestParam(name = "users", required = false) List<Integer> userIds,
            @RequestParam(name = "states", required = false) List<String> eventStates,
            @RequestParam(name = "categories", required = false) List<Integer> categoryIds,
            @RequestParam(name = "rangeStart", required = false) String startRange,
            @RequestParam(name = "rangeEnd", required = false) String endRange,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Запрашиваем список событий администратором. users:{}, states:{}, categories:{}, rangeStart:{}, rangeEnd:{}",
                userIds, eventStates, categoryIds, startRange, endRange);

        return eventService.findEventsByAdmin(eventStates, userIds, categoryIds, startRange, endRange, from, size);
    }

    @PatchMapping("/{eid}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Integer eid,
                                  @RequestBody @Validated UpdateEventAdminRequest updateDto) {

        log.info("Редактируем событие id={}. {}", eid, updateDto);
        return eventService.updateEventByAdmin(eid, updateDto);
    }
}

