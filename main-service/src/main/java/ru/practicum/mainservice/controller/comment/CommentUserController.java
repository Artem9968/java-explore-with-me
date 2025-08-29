package ru.practicum.mainservice.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.comment.CommentCreateDto;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.service.comment.CommentService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class CommentUserController {

    private final CommentService commentService;

    @PostMapping("/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentForEvent(@PathVariable Integer userId,
                                            @PathVariable Integer eventId,
                                            @RequestBody @Validated CommentCreateDto commentDto) {
        log.info("Пользователь с id={} добавляет комментарий к событию id={}: {}", userId, eventId, commentDto);
        return commentService.createComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Integer userId,
                                    @PathVariable Integer commentId,
                                    @RequestBody @Validated CommentCreateDto commentCreateDto) {
        log.info("Пользователь с id={} редактирует комментарий id={}: {}", userId, commentId, commentCreateDto);
        return commentService.updateComment(userId, commentId, commentCreateDto);
    }

    @GetMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> listUserComments(@PathVariable Integer userId,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получение списка комментариев пользователя с id={}", userId);
        return commentService.findCommentsByAuthor(userId, from, size);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@PathVariable Integer commentId,
                              @PathVariable Integer userId) {
        log.info("Пользователь с id={} удаляет комментарий id={}", userId, commentId);
        commentService.removeComment(userId, commentId);
    }

}

