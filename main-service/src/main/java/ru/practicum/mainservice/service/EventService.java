package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.EventFullDto;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.NewEventDto;
import ru.practicum.mainservice.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.model.Event;

import java.util.List;

public interface EventService {
    EventFullDto createEvent(NewEventDto newEventDto, Integer userId);

    EventFullDto getEventById(Integer eventId, Integer userId);

    List<EventShortDto> getEventsByUserId(Integer userId, Integer from, Integer size);

    EventFullDto patchEvent(Integer eventId, UpdateEventUserRequest eventDto, Integer userId);

    EventFullDto adminUpdateEvent(Integer eventId, UpdateEventAdminRequest eventDto);

    Event findEventById(Integer eventId);

    List<EventShortDto> findEventsByParametrs(String text,
                                              List<Integer> categories,
                                              Boolean paid,
                                              String rangeStart,
                                              String rangeEnd,
                                              Boolean onlyAvailable,
                                              String sort,
                                              Integer from, Integer size);

    List<EventFullDto> findEventsByAdmin(List<String> states,
                                         List<Integer> users,
                                         List<Integer> categories,
                                         String rangeStart,
                                         String rangeEnd,
                                         Integer from,
                                         Integer size);

    List<Event> findEventsByIdIn(List<Integer> eventIds);
}