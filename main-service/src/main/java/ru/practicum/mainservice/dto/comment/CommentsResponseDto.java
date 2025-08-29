package ru.practicum.mainservice.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommentsResponseDto {

    private List<CommentDto> allComments = new ArrayList<>();

}
