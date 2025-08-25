package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.model.enums.RequestState;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RequestStatusUpdate {

    private RequestState newStatus;

    private List<Integer> requestIds = new ArrayList<>();
}
