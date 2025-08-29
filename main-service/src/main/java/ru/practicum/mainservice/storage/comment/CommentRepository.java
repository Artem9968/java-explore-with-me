package ru.practicum.mainservice.storage.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.mainservice.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>,
        JpaSpecificationExecutor<Comment> {

    List<Comment> findByEventId(Integer eventId);

    List<Comment> findByAuthorId(Integer userId);

}
