package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import ru.practicum.mainservice.model.enums.EventStatus;
import ru.practicum.mainservice.model.Coordinates;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventFullDto {

    private Integer viewCount;

    @Size(min = 3, max = 120, message = "Заголовок должен содержать от 3 до 120 символов")
    private String title;

    private EventStatus eventStatus;

    private Boolean requiresApproval;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publicationTime;

    private Integer maxAttendees;

    private Boolean isPaid;

    private Coordinates location;

    private UserShortDto organizer;

    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTimestamp;

    private Integer approvedParticipantsCount;

    private CategoryResponse category;

    private String annotation;
}
