package ru.practicum.mainservice.mapper.event;

import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.mapper.user.UserMapper;
import ru.practicum.mainservice.mapper.category.CategoryMapper;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.event.Location;
import ru.practicum.mainservice.model.enums.EventState;

public class EventMapper {

    public static EventShortDto toShortDto(Event event) {
        EventShortDto result = new EventShortDto();
        result.setId(event.getId());
        result.setTitle(event.getTitle());
        result.setAnnotation(event.getAnnotation());
        result.setCategory(CategoryMapper.toDto(event.getCategory()));
        result.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        result.setEventDate(event.getEventDate());
        result.setPaid(event.getPaid());
        result.setParticipantLimit(event.getParticipantLimit() != null ? event.getParticipantLimit() : 0);
        result.setConfirmedRequests(event.getCachedConfirmedRequests() != null ? event.getCachedConfirmedRequests() : 0);
        result.setViews(event.getCachedViews() != null ? event.getCachedViews() : 0);
        return result;
    }

    public static EventFullDto toFullDto(Event event) {
        EventFullDto result = new EventFullDto();
        result.setId(event.getId());
        result.setAnnotation(event.getAnnotation());
        result.setDescription(event.getDescription());
        result.setEventDate(event.getEventDate());
        result.setCreatedOn(event.getCreatedOn());
        result.setCategory(CategoryMapper.toDto(event.getCategory()));
        result.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        result.setLocation(new Location(event.getLat(), event.getLon()));
        result.setParticipantLimit(event.getParticipantLimit() != null ? event.getParticipantLimit() : 0);
        result.setRequestModeration(event.getRequestModeration());
        result.setState(event.getState());
        result.setPaid(event.getPaid());
        result.setPublishedOn(event.getPublishedOn());
        result.setTitle(event.getTitle());
        result.setConfirmedRequests(event.getCachedConfirmedRequests() != null ? event.getCachedConfirmedRequests() : 0);
        result.setViews(event.getCachedViews() != null ? event.getCachedViews() : 0);
        return result;
    }

    public static Event toEvent(NewEventDto newDto) {
        Event result = new Event();
        result.setAnnotation(newDto.getAnnotation());
        result.setDescription(newDto.getDescription());
        result.setEventDate(newDto.getEventDate());
        result.setCreatedOn(java.time.LocalDateTime.now());
        result.setLat(newDto.getLocation().getLat());
        result.setLon(newDto.getLocation().getLon());
        result.setPaid(newDto.getPaid() != null ? newDto.getPaid() : false);
        result.setParticipantLimit(newDto.getParticipantLimit() != null ? newDto.getParticipantLimit() : 0);
        result.setRequestModeration(newDto.getRequestModeration() != null ? newDto.getRequestModeration() : true);
        result.setState(EventState.PENDING);
        result.setTitle(newDto.getTitle());
        result.setCachedConfirmedRequests(0);
        result.setCachedViews(0);
        return result;
    }
}

