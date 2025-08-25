package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import ru.practicum.mainservice.model.Coordinates;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EventCreateRequest {

    @Size(min = 3, max = 120, message = "Заголовок должен содержать от 3 до 120 символов")
    @NotBlank(message = "Заголовок не может состоять из пробелов")
    @NotEmpty(message = "Заголовок обязателен для заполнения")
    private String title;

    @Size(min = 20, max = 2000, message = "Аннотация должна содержать от 20 до 2000 символов")
    @NotBlank(message = "Аннотация не может состоять из пробелов")
    @NotEmpty(message = "Аннотация обязательна для заполнения")
    private String annotation;

    @Size(min = 20, max = 7000, message = "Описание должно содержать от 20 до 7000 символов")
    @NotBlank(message = "Описание не может состоять из пробелов")
    @NotEmpty(message = "Описание обязательно для заполнения")
    private String description;

    @NotNull(message = "Идентификатор категории обязателен")
    private Integer categoryId;

    @Future(message = "Дата события должна быть будущей")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Координаты места проведения обязательны")
    private Coordinates location;

    private Boolean isPaid;

    @PositiveOrZero(message = "Лимит участников не может быть отрицательным")
    private Integer maxAttendees;

    private Boolean requiresApproval;
}
