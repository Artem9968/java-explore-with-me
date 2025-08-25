package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.mainservice.enums.RequestStatus;


import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RequestUpdateDto {
    List<Integer> requestIds = new ArrayList<>();
    RequestStatus status;
}
