package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.mainservice.model.enums.RequestState;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestResponse {

    private RequestState status;

    private Integer requesterId;

    private Integer eventId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    private Integer id;
}
