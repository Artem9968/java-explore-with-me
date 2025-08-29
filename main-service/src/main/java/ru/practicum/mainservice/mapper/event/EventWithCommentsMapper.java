package ru.practicum.mainservice.mapper.event;

import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.event.EventWithCommentsDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.model.event.Event;

import java.util.List;

public class EventWithCommentsMapper {

    public static EventWithCommentsDto toDto(Event event, List<CommentDto> comments) {
        return EventWithCommentsDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .confirmedRequests(event.getCachedConfirmedRequests())
                .views(event.getCachedViews())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .comments(comments)
                .commentsCount(comments != null ? comments.size() : 0)
                .build();
    }
}
