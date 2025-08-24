package ru.practicum.mainservice.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Integer id;
    @Size(min = 1, max = 50, message = "Максимальная длина описания - 50 символов.")
    private String name;
}
