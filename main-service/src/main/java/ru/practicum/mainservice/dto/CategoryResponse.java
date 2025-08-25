package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    @Size(min = 1, max = 50, message = "Название категории должно быть от 1 до 50 символов")
    private String name;

    private Integer id;
}
