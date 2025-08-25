package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.EventFullDto;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.EventCreateRequest;
import ru.practicum.mainservice.model.enums.EventStatus;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.Coordinates;

import java.time.LocalDateTime;

public class EventMapper {
    private EventMapper() {
    }

    public static Event toEvent(final EventCreateRequest newDto) {
        Event event = new Event();
        event.setAnnotation(newDto.getAnnotation());
        event.setDescription(newDto.getDescription());
        event.setScheduledTime(newDto.getEventDate());
        event.setCreationTimestamp(LocalDateTime.now());
        event.setLatitude(newDto.getLocation().getLatitude());
        event.setLongitude(newDto.getLocation().getLongitude());
        event.setIsPaid(false);
        if (newDto.getIsPaid() != null) {
            event.setIsPaid(newDto.getIsPaid());
        }
        event.setMaxAttendees(0);
        if (newDto.getMaxAttendees() != null) {
            event.setMaxAttendees(newDto.getMaxAttendees());
        }
        event.setRequiresApproval(true);
        if (newDto.getRequiresApproval() != null) {
            event.setRequiresApproval(newDto.getRequiresApproval());
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
        dto.setCreationTimestamp(event.getCreationTimestamp());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setOrganizer(UserMapper.toUserShortDto(event.getOrganizer()));
        dto.setLocation(new Coordinates(event.getLatitude(), event.getLongitude()));
        dto.setMaxAttendees(0);
        if (event.getMaxAttendees() != null) {
            dto.setMaxAttendees(event.getMaxAttendees());
        }
        dto.setRequiresApproval(event.getRequiresApproval());
        dto.setEventStatus(event.getState());
        dto.setIsPaid(event.getIsPaid());
        dto.setPublicationTime(event.getPublicationTime());
        dto.setTitle(event.getTitle());
        dto.setApprovedParticipantsCount(0);
        if (event.getApprovedParticipants() != null) {
            dto.setApprovedParticipantsCount(event.getApprovedParticipants());
        }
        dto.setViewCount(0);
        if (event.getViewCount() != null) {
            dto.setViewCount(event.getViewCount());
        }
        return dto;
    }

    public static EventShortDto toShortDto(Event event) {
        EventShortDto dto = new EventShortDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toDto(event.getCategory()));
        dto.setOrganizer(UserMapper.toUserShortDto(event.getOrganizer()));
        dto.setEventDate(event.getScheduledTime());
        dto.setIsPaid(event.getIsPaid());
        dto.setMaxAttendees(0);
        if (event.getMaxAttendees() != null) {
            dto.setMaxAttendees(event.getMaxAttendees());
        }
        dto.setApprovedParticipantsCount(0);
        if (event.getApprovedParticipants() != null) {
            dto.setApprovedParticipantsCount(event.getApprovedParticipants());
        }
        dto.setViewCount(0);
        if (event.getViewCount() != null) {
            dto.setViewCount(event.getViewCount());
        }
        return dto;
    }
}
