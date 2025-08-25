package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @Size(min = 6, max = 254, message = "Email должен содержать от 6 до 254 символов")
    @Email(message = "Некорректный формат email адреса")
    @NotBlank(message = "Email обязателен для заполнения")
    private String emailAddress;

    @Size(min = 2, max = 250, message = "Имя должно содержать от 2 до 250 символов")
    @NotBlank(message = "Имя обязательно для заполнения")
    private String name;

    private Integer id;
}
