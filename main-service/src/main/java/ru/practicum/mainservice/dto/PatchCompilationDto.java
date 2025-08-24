package ru.practicum.mainservice.dto;

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
public class PatchCompilationDto {
    private List<Integer> events = new ArrayList<>();
    @Size(min = 2, max = 50, message = "длина описания 2 - 50 символов.")
    private String title;
    private Boolean pinned;

}
