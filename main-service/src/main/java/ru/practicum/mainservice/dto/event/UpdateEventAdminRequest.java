package ru.practicum.mainservice.dto.event;

import lombok.ToString;
import java.time.LocalDateTime;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.practicum.mainservice.model.enums.EventAdminStateAction;
import ru.practicum.mainservice.model.event.Location;

@NoArgsConstructor
@Getter
@ToString
@Setter
public class UpdateEventAdminRequest {
    @Size(min = 3, max = 120)
    private String title;

    @Size(min = 20, max = 2000)
    private String annotation;

    @Size(min = 20, max = 7000)
    private String description;

    private Integer category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @Positive
    private Integer participantLimit;

    private Boolean requestModeration;

    private EventAdminStateAction stateAction;
}
