package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCollectionUpdateRequest {

    private Boolean isPinned;

    @Size(min = 2, max = 50, message = "Длина заголовка должна быть от 2 до 50 символов")
    private String collectionTitle;

    private List<Integer> eventIds = new ArrayList<>();
}
