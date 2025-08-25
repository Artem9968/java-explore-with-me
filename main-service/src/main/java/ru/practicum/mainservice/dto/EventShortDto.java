package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EventShortDto {

    private Integer viewCount;

    private String title;

    private Boolean isPaid;

    private UserShortDto organizer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Integer approvedParticipantsCount;

    private CategoryResponse category;

    private String annotation;

    @JsonIgnore
    private Integer maxAttendees;

    private Integer id;
}
