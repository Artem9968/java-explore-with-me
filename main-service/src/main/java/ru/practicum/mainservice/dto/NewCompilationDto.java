package ru.practicum.mainservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Integer> events = new ArrayList<>();
    @NotEmpty(message = "Заголовок подборки не может быть пустым.")
    @NotBlank(message = "Заголовок подборки не может быть пустым.")
    @Size(min = 2, max = 50, message = "длина описания 2 - 50 символов.")
    private String title;
    private Boolean pinned;
}
