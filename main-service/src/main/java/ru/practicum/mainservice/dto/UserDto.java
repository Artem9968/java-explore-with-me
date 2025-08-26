package ru.practicum.mainservice.dto;

import lombok.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class UserDto {
    private Integer id;

    @Size(min = 2, max = 250)
    @NotBlank
    private String name;

    @Size(min = 6, max = 254)
    @Email
    @NotBlank
    private String email;
}
