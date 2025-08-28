package ru.practicum.mainservice.dto.event;

import lombok.Setter;
import java.time.LocalDateTime;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.model.event.Location;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.model.enums.EventState;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class EventFullDto {
    private Integer id;

    @Size(min = 3, max = 120)
    private String title;

    private String annotation;

    private String description;

    private CategoryDto category;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private EventState state;

    private Integer confirmedRequests;

    private Integer views;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
}
