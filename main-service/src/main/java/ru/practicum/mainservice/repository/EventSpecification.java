package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.mainservice.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public class EventSpecification {
    public static Specification<Event> annotetionContains(String text) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("annotation"), "%" + text + "%"));
    }

    public static Specification<Event> descriptionContains(String text) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("description"), "%" + text + "%"));
    }

    public static Specification<Event> categoryIn(List<Integer> categories) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.join("category").get("id")).value(categories));
    }

    public static Specification<Event> paidEqual(Boolean paid) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("paid"), paid));
    }

    public static Specification<Event> eventDateAfter(LocalDateTime startDate) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startDate));
    }

    public static Specification<Event> eventDateBefore(LocalDateTime endDate) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("eventDate"), endDate));
    }

    public static Specification<Event> eventInitiatorIdIn(List<Integer> initiatorIds) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.join("initiator").get("id")).value(initiatorIds));
    }

    public static Specification<Event> eventStateIn(List<String> states) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("state")).value(states));
    }
}
