package ru.practicum.mainservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserShortDto {
    private Integer id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
}
