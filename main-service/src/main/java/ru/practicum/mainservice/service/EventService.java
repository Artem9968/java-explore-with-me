package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.EventFullDto;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.NewEventDto;
import ru.practicum.mainservice.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.dto.UpdateEventUserRequest;

import ru.practicum.mainservice.model.Event;

import java.util.List;

public interface EventService {
    List<Event> findEventsByIdIn(List<Integer> eventIds);

    List<EventFullDto> findEventsByAdmin(List<String> states,
                                         List<Integer> users,
                                         List<Integer> categories,
                                         String rangeStart,
                                         String rangeEnd,
                                         Integer from,
                                         Integer size);

    List<EventShortDto> findEventsByParameters(String text,
                                               List<Integer> categories,
                                               Boolean paid,
                                               String rangeStart,
                                               String rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               Integer from, Integer size);

    Event findEventById(Integer eventId);

    EventFullDto updateEventByAdmin(Integer eventId, UpdateEventAdminRequest eventDto);

    EventFullDto updateEventByUser(Integer eventId, UpdateEventUserRequest eventDto, Integer userId);

    List<EventShortDto> findUserEvents(Integer userId, Integer from, Integer size);

    EventFullDto findUserEventById(Integer eventId, Integer userId);

    EventFullDto createEvent(NewEventDto newEventDto, Integer userId);
}
