package ru.practicum.mainservice.mapper.compilation;

import ru.practicum.mainservice.mapper.event.EventMapper;
import ru.practicum.mainservice.model.compilation.Compilation;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;

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
