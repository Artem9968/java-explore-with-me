package ru.practicum.mainservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NewCategoryDto {
    @Size(min = 1, max = 50)
    private String name;
}