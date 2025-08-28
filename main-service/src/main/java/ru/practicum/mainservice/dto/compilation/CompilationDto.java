package ru.practicum.mainservice.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.dto.event.EventShortDto;

import java.util.List;
import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompilationDto {

    private int id;

    private String title;

    private Boolean pinned;

    private List<EventShortDto> events = new ArrayList<>();

}
