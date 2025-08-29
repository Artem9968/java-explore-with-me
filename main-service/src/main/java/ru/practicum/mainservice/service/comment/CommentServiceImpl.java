package ru.practicum.mainservice.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.mainservice.dto.comment.CommentCreateDto;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidationException;

import ru.practicum.mainservice.mapper.comment.CommentMapper;
import ru.practicum.mainservice.model.comment.Comment;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.service.user.UserService;

import ru.practicum.mainservice.storage.comment.CommentRepository;
import ru.practicum.mainservice.storage.comment.CommentSpecification;
import ru.practicum.mainservice.storage.event.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Integer maxCommentHours = 48;

    @Override
    @Transactional
    public CommentDto createComment(Integer authorId, Integer eventId, CommentCreateDto commentCreateDto) {
        User author = userService.findUserById(authorId);
        Event eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        validateEventForComment(eventEntity);

        Comment entity = CommentMapper.fromCreateDto(commentCreateDto);
        entity.setAuthor(author);
        entity.setEventId(eventId);

        Comment persisted = commentRepository.save(entity);
        return CommentMapper.toDto(persisted);
    }

    private void validateEventForComment(Event event) {
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new ValidationException("Комментирование доступно только для опубликованных событий");
        }
        if (event.getEventDate().plusHours(maxCommentHours).isBefore(LocalDateTime.now())) {
            throw new ValidationException("Комментирование возможно только в течение " + maxCommentHours + " часов после начала события");
        }
    }

    @Override
    @Transactional
    public CommentDto updateComment(Integer authorId, Integer commentId, CommentCreateDto commentCreateDto) {
        User author = userService.findUserById(authorId);
        Comment entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        if (!entity.getAuthor().getId().equals(author.getId())) {
            throw new ValidationException("Редактировать комментарий может только его автор");
        }

        entity.setText(commentCreateDto.getText());
        entity.setEditedOn(LocalDateTime.now());

        Comment updated = commentRepository.save(entity);
        return CommentMapper.toDto(updated);
    }

    @Override
    public CommentDto findCommentById(Integer commentId) {
        Comment entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        return CommentMapper.toDto(entity);
    }

    @Override
    public List<CommentDto> findCommentsByEvent(Integer eventId,
                                                String text,
                                                List<Integer> authorIds,
                                                String rangeStart,
                                                String rangeEnd,
                                                String sort,
                                                Integer from,
                                                Integer size) {

        LocalDateTime start = parseDateTime(rangeStart);
        LocalDateTime end = parseDateTime(rangeEnd);

        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("Начальная дата не может быть позже конечной даты");
        }

        Specification<Comment> specification = Specification.where(CommentSpecification.byEventId(eventId));

        if (text != null) specification = specification.and(CommentSpecification.byTextContaining(text));
        if (authorIds != null) specification = specification.and(CommentSpecification.byAuthorIds(authorIds));
        if (start != null) specification = specification.and(CommentSpecification.createdAfter(start));
        if (end != null) specification = specification.and(CommentSpecification.createdBefore(end));

        List<Comment> commentList = "OLD".equalsIgnoreCase(sort)
                ? commentRepository.findAll(specification, Sort.by("createdOn"))
                : commentRepository.findAll(specification, Sort.by("createdOn").descending());

        return commentList.stream()
                .skip(from)
                .limit(size)
                .map(CommentMapper::toDto)
                .toList();
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return LocalDateTime.parse(value, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Неверный формат даты/времени: " + e.getMessage());
        }
    }

    @Override
    public List<CommentDto> findCommentsByAuthor(Integer authorId, Integer from, Integer size) {
        return commentRepository.findByAuthorId(authorId).stream()
                .skip(from)
                .limit(size)
                .map(CommentMapper::toDto)
                .toList();
    }

        @Override
    @Transactional
    public void removeComment(Integer authorId, Integer commentId) {
        Comment entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        if (!entity.getAuthor().getId().equals(authorId)) {
            throw new ValidationException("Пользователь не является автором комментария");
        }

        commentRepository.delete(entity);
    }
}

