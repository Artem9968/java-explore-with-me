package ru.practicum.mainservice.dto.user;

import lombok.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class UserShortDto {
    private Integer id;

    @NotBlank
    private String name;
}
