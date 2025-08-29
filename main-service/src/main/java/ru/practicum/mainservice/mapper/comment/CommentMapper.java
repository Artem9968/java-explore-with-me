package ru.practicum.mainservice.mapper.comment;

import ru.practicum.mainservice.dto.comment.CommentCreateDto;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.mapper.user.UserMapper;
import ru.practicum.mainservice.model.comment.Comment;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment fromCreateDto(CommentCreateDto commentCreateDto) {
        Comment comment = new Comment();
        comment.setText(commentCreateDto.getText());
        comment.setCreatedOn(LocalDateTime.now());
        return comment;
    }

    public static CommentDto toDto(Comment result) {
        CommentDto dto = new CommentDto();
        dto.setId(result .getId());
        dto.setAuthor(UserMapper.toUserDto(result .getAuthor()));
        dto.setEventId(result .getEventId());
        dto.setText(result .getText());
        dto.setCreatedOn(result .getCreatedOn());
        dto.setEditedOn(result .getEditedOn());
        return dto;
    }
}

