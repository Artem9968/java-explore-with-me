package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import ru.practicum.mainservice.model.enums.UserEventAction;
import ru.practicum.mainservice.model.Coordinates;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EventUserUpdateRequest {

    private UserEventAction userAction;

    @Size(min = 3, max = 120, message = "Заголовок должен содержать от 3 до 120 символов")
    private String title;

    @Size(min = 20, max = 2000, message = "Аннотация должна содержать от 20 до 2000 символов")
    private String annotation;

    @Size(min = 20, max = 7000, message = "Описание должно содержать от 20 до 7000 символов")
    private String description;

    private Integer categoryId;

    @Future(message = "Дата события должна быть будущей")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Coordinates location;

    private Boolean isPaid;

    @Positive(message = "Лимит участников должен быть положительным числом")
    private Integer maxAttendees;

    private Boolean requiresApproval;
}
