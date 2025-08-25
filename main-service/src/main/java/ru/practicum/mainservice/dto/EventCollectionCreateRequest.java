package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCollectionCreateRequest {

    @Size(min = 2, max = 50, message = "Длина заголовка должна быть от 2 до 50 символов")
    @NotBlank(message = "Заголовок коллекции не может состоять из пробелов")
    @NotEmpty(message = "Заголовок коллекции обязателен для заполнения")
    private String collectionTitle;

    private Boolean isPinned;

    private List<Integer> eventIds = new ArrayList<>();
}
