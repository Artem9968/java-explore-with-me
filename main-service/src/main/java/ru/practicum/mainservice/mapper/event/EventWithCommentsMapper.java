package ru.practicum.mainservice.mapper.event;

import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.model.event.Event;

import java.util.List;

public class EventWithCommentsMapper {

    public static EventFullDto toDto(Event event, List<CommentDto> comments) {
        List<CommentDto> safeComments = comments != null ? comments : List.of();

        EventFullDto dto = new EventFullDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setAnnotation(event.getAnnotation());
        dto.setDescription(event.getDescription());
        dto.setCategory(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()));
        dto.setInitiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()));
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setRequestModeration(event.getRequestModeration());
        dto.setState(event.getState());
        dto.setConfirmedRequests(event.getCachedConfirmedRequests());
        dto.setViews(event.getCachedViews());
        dto.setEventDate(event.getEventDate());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setComments(safeComments);
        dto.setCommentsCount(safeComments.size());

        return dto;
    }
}
