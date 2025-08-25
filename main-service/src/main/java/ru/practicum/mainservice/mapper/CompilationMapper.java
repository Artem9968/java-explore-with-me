package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.CompilationDto;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.NewCompilationDto;
import ru.practicum.mainservice.model.EventCollection;

import java.util.List;

public class CompilationMapper {
    private CompilationMapper() {
    }

    public static EventCollection toCompilation(NewCompilationDto dto) {
        EventCollection c = new EventCollection();
        c.setCollectionTitle(dto.getTitle());
        c.setIsPinned(false);
        if (dto.getPinned() != null) {
            c.setIsPinned(dto.getPinned());
        }
        return c;
    }

    public static CompilationDto toCompilationDto(EventCollection c) {
        CompilationDto dto = new CompilationDto();
        dto.setId(c.getId());
        dto.setTitle(c.getCollectionTitle());
        dto.setPinned(c.getIsPinned());
        List<EventShortDto> eventDtos = c.getEvents()
                .stream()
                .map(EventMapper::toShortDto)
                .toList();
        dto.setEvents(eventDtos);
        return dto;
    }

}
