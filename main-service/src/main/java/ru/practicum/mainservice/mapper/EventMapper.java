package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.EventFullDto;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.NewEventDto;
import ru.practicum.mainservice.enums.EventState;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.Location;

import java.time.LocalDateTime;

public class EventMapper {
    private EventMapper() {
    }

    public static Event toEvent(final NewEventDto newDto) {
        Event event = new Event();
        event.setAnnotation(newDto.getAnnotation());
        event.setDescription(newDto.getDescription());
        event.setEventDate(newDto.getEventDate());
        event.setCreatedOn(LocalDateTime.now());
        event.setLat(newDto.getLocation().getLat());
        event.setLon(newDto.getLocation().getLon());
        event.setPaid(false);
        if (newDto.getPaid() != null) {
            event.setPaid(newDto.getPaid());
        }
        event.setParticipantLimit(0);
        if (newDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newDto.getParticipantLimit());
        }
        event.setRequestModeration(true);
        if (newDto.getRequestModeration() != null) {
            event.setRequestModeration(newDto.getRequestModeration());
        }
        event.setState(EventState.PENDING);
        event.setTitle(newDto.getTitle());
        event.setConfirmedRequests(0);
        event.setViews(0);
        return event;
    }

    public static EventFullDto toFullDto(Event event) {
        EventFullDto dto = new EventFullDto();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        dto.setLocation(new Location(event.getLat(), event.getLon()));
        dto.setParticipantLimit(0);
        if (event.getParticipantLimit() != null) {
            dto.setParticipantLimit(event.getParticipantLimit());
        }
        dto.setRequestModeration(event.getRequestModeration());
        dto.setState(event.getState());
        dto.setPaid(event.getPaid());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setTitle(event.getTitle());
        dto.setConfirmedRequests(0);
        if (event.getConfirmedRequests() != null) {
            dto.setConfirmedRequests(event.getConfirmedRequests());
        }
        dto.setViews(0);
        if (event.getViews() != null) {
            dto.setViews(event.getViews());
        }
        return dto;
    }

    public static EventShortDto toShortDto(Event event) {
        EventShortDto dto = new EventShortDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        dto.setEventDate(event.getEventDate());
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(0);
        if (event.getParticipantLimit() != null) {
            dto.setParticipantLimit(event.getParticipantLimit());
        }
        dto.setConfirmedRequests(0);
        if (event.getConfirmedRequests() != null) {
            dto.setConfirmedRequests(event.getConfirmedRequests());
        }
        dto.setViews(0);
        if (event.getViews() != null) {
            dto.setViews(event.getViews());
        }
        return dto;
    }
}
