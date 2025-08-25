package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.EventFullDto;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.NewEventDto;
import ru.practicum.mainservice.model.enums.EventStatus;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.Coordinates;

import java.time.LocalDateTime;

public class EventMapper {
    private EventMapper() {
    }

    public static Event toEvent(final NewEventDto newDto) {
        Event event = new Event();
        event.setAnnotation(newDto.getAnnotation());
        event.setDescription(newDto.getDescription());
        event.setScheduledTime(newDto.getEventDate());
        event.setCreationTimestamp(LocalDateTime.now());
        event.setLatitude(newDto.getCoordinates().getLat());
        event.setLongitude(newDto.getCoordinates().getLongitude());
        event.setIsPaid(false);
        if (newDto.getPaid() != null) {
            event.setIsPaid(newDto.getPaid());
        }
        event.setMaxAttendees(0);
        if (newDto.getParticipantLimit() != null) {
            event.setMaxAttendees(newDto.getParticipantLimit());
        }
        event.setRequiresApproval(true);
        if (newDto.getRequestModeration() != null) {
            event.setRequiresApproval(newDto.getRequestModeration());
        }
        event.setState(EventStatus.IN_MODERATION);
        event.setTitle(newDto.getTitle());
        event.setApprovedParticipants(0);
        event.setViewCount(0);
        return event;
    }

    public static EventFullDto toFullDto(Event event) {
        EventFullDto dto = new EventFullDto();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getScheduledTime());
        dto.setCreatedOn(event.getCreationTimestamp());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setInitiator(UserMapper.toUserShortDto(event.getOrganizer()));
        dto.setCoordinates(new Coordinates(event.getLatitude(), event.getLongitude()));
        dto.setParticipantLimit(0);
        if (event.getMaxAttendees() != null) {
            dto.setParticipantLimit(event.getMaxAttendees());
        }
        dto.setRequestModeration(event.getRequiresApproval());
        dto.setState(event.getState());
        dto.setPaid(event.getIsPaid());
        dto.setPublishedOn(event.getPublicationTime());
        dto.setTitle(event.getTitle());
        dto.setConfirmedRequests(0);
        if (event.getApprovedParticipants() != null) {
            dto.setConfirmedRequests(event.getApprovedParticipants());
        }
        dto.setViews(0);
        if (event.getViewCount() != null) {
            dto.setViews(event.getViewCount());
        }
        return dto;
    }

    public static EventShortDto toShortDto(Event event) {
        EventShortDto dto = new EventShortDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setInitiator(UserMapper.toUserShortDto(event.getOrganizer()));
        dto.setEventDate(event.getScheduledTime());
        dto.setPaid(event.getIsPaid());
        dto.setParticipantLimit(0);
        if (event.getMaxAttendees() != null) {
            dto.setParticipantLimit(event.getMaxAttendees());
        }
        dto.setConfirmedRequests(0);
        if (event.getApprovedParticipants() != null) {
            dto.setConfirmedRequests(event.getApprovedParticipants());
        }
        dto.setViews(0);
        if (event.getViewCount() != null) {
            dto.setViews(event.getViewCount());
        }
        return dto;
    }
}
