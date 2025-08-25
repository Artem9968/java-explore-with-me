package ru.practicum.mainservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.mainservice.enums.EventAdminAction;
import ru.practicum.mainservice.model.Location;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "длина аннотации 20 - 2000 символов.")
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000, message = "длина описания 20 - 7000 символов.")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "дата события не должна быть уже наступившей.")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @Positive(message = "число участников должно быть положительным")
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120, message = "длина заголовка 3 - 120 символов.")
    private String title;
    private EventAdminAction stateAction;
}
