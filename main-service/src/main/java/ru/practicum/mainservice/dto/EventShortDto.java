package ru.practicum.mainservice.dto;

import lombok.ToString;
import java.time.LocalDateTime;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@NoArgsConstructor
@Getter
@ToString
@Setter
public class EventShortDto {
    private Integer id;

    private String title;

    private String annotation;

    private CategoryDto category;

    private UserShortDto initiator;

    private Boolean paid;

    private Integer confirmedRequests;

    private Integer views;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @JsonIgnore
    private Integer participantLimit;
}
