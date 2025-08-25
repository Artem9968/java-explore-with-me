package ru.practicum.mainservice.repository;

import java.util.List;
import java.time.LocalDateTime;
import ru.practicum.mainservice.model.Event;
import org.springframework.data.jpa.domain.Specification;

public class EventSpecification {

    public static Specification<Event> eventStatusIn(List<String> statusList) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("eventStatus")).value(statusList));
    }

    public static Specification<Event> eventOrganizerIdIn(List<Integer> organizerIdList) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.join("organizer").get("id")).value(organizerIdList));
    }

    public static Specification<Event> scheduledTimeBefore(LocalDateTime endDateTime) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("scheduledTime"), endDateTime));
    }

    public static Specification<Event> scheduledTimeAfter(LocalDateTime startDateTime) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("scheduledTime"), startDateTime));
    }

    public static Specification<Event> isPaidEqual(Boolean isPaidFlag) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isPaid"), isPaidFlag));
    }

    public static Specification<Event> categoryIn(List<Integer> categoryIdList) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.join("category").get("id")).value(categoryIdList));
    }

    public static Specification<Event> descriptionContains(String searchText) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("description"), "%" + searchText + "%"));
    }

    public static Specification<Event> annotationContains(String annotationText) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("briefDescription"), "%" + annotationText + "%"));
    }
}
