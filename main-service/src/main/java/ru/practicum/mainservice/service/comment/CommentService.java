package ru.practicum.mainservice.service.comment;

import ru.practicum.mainservice.dto.comment.CommentCreateDto;
import ru.practicum.mainservice.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Integer authorId, Integer eventId, CommentCreateDto commentCreateDto);

    CommentDto updateComment(Integer authorId, Integer commentId, CommentCreateDto commentCreateDto);

    CommentDto findCommentById(Integer commentId);

    List<CommentDto> findCommentsByEvent(Integer eventId,
                                         String text,
                                         List<Integer> authorIds,
                                         String rangeStart,
                                         String rangeEnd,
                                         String sort,
                                         Integer from,
                                         Integer size);

    List<CommentDto> findCommentsByAuthor(Integer authorId, Integer from, Integer size);

    void removeComment(Integer authorId, Integer commentId);
}
