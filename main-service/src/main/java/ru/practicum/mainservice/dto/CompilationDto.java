package ru.practicum.mainservice.dto;

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
public class CompilationDto {
    private int id;
    private List<EventShortDto> events = new ArrayList<>();
    private String title;
    private Boolean pinned;
}
