package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.EventCollectionResponse;
import ru.practicum.mainservice.dto.EventCollectionCreateRequest;
import ru.practicum.mainservice.dto.EventCollectionUpdateRequest;

import java.util.List;

public interface CompilationService {
    EventCollectionResponse createCompilation(EventCollectionCreateRequest compilationDto);

    EventCollectionResponse patchCompilation(Integer compId, EventCollectionUpdateRequest compilationDto);

    void deleteCompilation(Integer compId);

    List<EventCollectionResponse> getCompilations(Boolean pinned, Integer from, Integer size);

    EventCollectionResponse getCompilation(Integer compId);
}
