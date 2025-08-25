package ru.practicum.mainservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.mainservice.model.Coordinates;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class NewEventDto {
    @NotEmpty(message = "Аннотация не может быть пустой.")
    @NotBlank(message = "Аннотация не может быть пустой.")
    @Size(min = 20, max = 2000, message = "длина аннотации 20 - 2000 символов.")
    private String annotation;
    @NotNull(message = "Категоря должна быть определена")
    private Integer category;
    @NotEmpty(message = "Описание события не может быть пустым.")
    @NotBlank(message = "Описание события не может быть пустым.")
    @Size(min = 20, max = 7000, message = "длина описания 20 - 7000 символов.")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "дата события не должна быть уже наступившей.")
    private LocalDateTime eventDate;
    @NotNull(message = "Место события должно быть определено.")
    private Coordinates coordinates;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotEmpty(message = "Заголовок не может быть пустым.")
    @NotBlank(message = "Заголовок не может быть пустым.")
    @Size(min = 3, max = 120, message = "длина заголовка 3 - 120 символов.")
    private String title;
}
