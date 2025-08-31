package ru.practicum.mainservice.storage.comment;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.mainservice.model.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;

public class CommentSpecification {

    public static Specification<Comment> byEventId(Integer eventId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("eventId"), eventId);
    }

    public static Specification<Comment> byTextContaining(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("text"), "%" + keyword + "%");
    }

    public static Specification<Comment> byAuthorIds(List<Integer> authorIds) {
        return (root, query, criteriaBuilder) -> root.join("author").get("id").in(authorIds);
    }

    public static Specification<Comment> createdAfter(LocalDateTime start) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdOn"), start);
    }

    public static Specification<Comment> createdBefore(LocalDateTime end) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("createdOn"), end);
    }

}


