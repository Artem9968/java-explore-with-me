package ru.practicum.mainservice.repository.event;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.mainservice.model.event.Event;

public class EventSpecification {

    public static Specification<Event> withStateIn(List<String> states) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("state")).value(states));
    }

    public static Specification<Event> withInitiatorIdIn(List<Integer> initiatorIds) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.join("initiator").get("id")).value(initiatorIds));
    }

    public static Specification<Event> withEventDateBefore(LocalDateTime endDate) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("eventDate"), endDate));
    }

    public static Specification<Event> withEventDateAfter(LocalDateTime startDate) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startDate));
    }

    public static Specification<Event> hasPaid(Boolean paid) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("paid"), paid));
    }

    public static Specification<Event> hasCategoryIn(List<Integer> categories) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.join("category").get("id")).value(categories));
    }

    public static Specification<Event> hasDescriptionWithText(String text) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("description"), "%" + text + "%"));
    }

    public static Specification<Event> hasAnnotationWithText(String text) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("annotation"), "%" + text + "%"));
    }

}
