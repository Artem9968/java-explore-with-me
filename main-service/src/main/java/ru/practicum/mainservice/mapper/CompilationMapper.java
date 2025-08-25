package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.EventCollectionResponse;
import ru.practicum.mainservice.dto.EventShortDto;
import ru.practicum.mainservice.dto.EventCollectionCreateRequest;
import ru.practicum.mainservice.model.EventCollection;

import java.util.List;

public class CompilationMapper {
    private CompilationMapper() {
    }

    public static EventCollection toCompilation(EventCollectionCreateRequest dto) {
        EventCollection c = new EventCollection();
        c.setCollectionTitle(dto.getCollectionTitle());
        c.setIsPinned(false);
        if (dto.getIsPinned() != null) {
            c.setIsPinned(dto.getIsPinned());
        }
        return c;
    }

    public static EventCollectionResponse toCompilationDto(EventCollection c) {
        EventCollectionResponse dto = new EventCollectionResponse();
        dto.setId(c.getId());
        dto.setCollectionTitle(c.getCollectionTitle());
        dto.setIsPinned(c.getIsPinned());
        List<EventShortDto> eventDtos = c.getEvents()
                .stream()
                .map(EventMapper::toShortDto)
                .toList();
        dto.setEvents(eventDtos);
        return dto;
    }

}
