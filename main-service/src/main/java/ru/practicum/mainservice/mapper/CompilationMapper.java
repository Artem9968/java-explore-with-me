package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.CompilationDto;
import ru.practicum.mainservice.dto.NewCompilationDto;

import java.util.List;

public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto result = new CompilationDto();
        result.setId(compilation.getId());
        result.setTitle(compilation.getTitle());
        result.setPinned(compilation.getPinned());

        List<EventShortDto> shortEvents = compilation.getEvents()
                .stream()
                .map(EventMapper::toShortDto)
                .toList();
        result.setEvents(shortEvents);

        return result;
    }

    public static Compilation toCompilation(NewCompilationDto newDto) {
        Compilation result = new Compilation();
        result.setTitle(newDto.getTitle());
        result.setPinned(newDto.getPinned() != null ? newDto.getPinned() : false);
        return result;
    }
}
