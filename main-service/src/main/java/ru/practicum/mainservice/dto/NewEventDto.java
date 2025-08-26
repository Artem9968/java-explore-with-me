package ru.practicum.mainservice.dto;

import lombok.ToString;
import java.time.LocalDateTime;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ru.practicum.mainservice.model.Location;

@NoArgsConstructor
@Getter
@ToString
@Setter
public class NewEventDto {
    @Size(min = 3, max = 120)
    @NotBlank
    private String title;

    @Size(min = 20, max = 2000)
    @NotBlank
    private String annotation;

    @Size(min = 20, max = 7000)
    @NotBlank
    private String description;

    @NotNull
    private Integer category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;
}
