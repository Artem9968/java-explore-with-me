package ru.practicum.mainservice.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class CommentAdminController {

    private final CommentService commentService;

    @GetMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getEventComments(@PathVariable Integer eventId,
                                             @RequestParam(name = "text", required = false) String text,
                                             @RequestParam(name = "authorIds", required = false) List<Integer> authorIds,
                                             @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                             @RequestParam(name = "sort", defaultValue = "new") String sort,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получение комментариев к событию id={}", eventId);
        return commentService.findCommentsByEvent(eventId, text, authorIds, rangeStart, rangeEnd, sort, from, size);
    }

    @PatchMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto editComment(@PathVariable Integer eventId,
                                  @PathVariable Integer commentId,
                                  @RequestBody CommentCreateDto updateDto) {
        log.info("Редактирование комментария id={} к событию id={}", commentId, eventId);
        return commentService.updateCommentByAdmin(eventId, commentId, updateDto);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer eventId,
                              @PathVariable Integer commentId) {
        log.info("Удаление комментария id={} к событию id={}", commentId, eventId);
        commentService.removeCommentByAdmin(eventId, commentId);
    }
}
