package ru.practicum.mainservice.dto.request;

import lombok.ToString;
import java.time.LocalDateTime;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import ru.practicum.mainservice.model.enums.RequestStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ParticipationRequestDto {

    private Integer id;

    private Integer requester;

    private Integer event;

    private RequestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

}
