package ru.practicum.mainservice.dto;

import lombok.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.enums.RequestStatus;
import java.util.List;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
@ToString
@Setter
public class RequestUpdateDto {
    List<Integer> requestIds = new ArrayList<>();

    RequestStatus status;
}
