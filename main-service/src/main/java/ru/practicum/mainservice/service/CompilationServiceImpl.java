package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.EventCollectionResponse;
import ru.practicum.mainservice.dto.EventCollectionCreateRequest;
import ru.practicum.mainservice.dto.EventCollectionUpdateRequest;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.CompilationMapper;
import ru.practicum.mainservice.model.EventCollection;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.repository.CompilationRepository;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Override
    public EventCollectionResponse createCompilation(EventCollectionCreateRequest compilationDto) {
        EventCollection eventCollection = CompilationMapper.toCompilation(compilationDto);
        List<Event> events = eventService.findEventsByIdIn(compilationDto.getEventIds());
        eventCollection.setEvents(new HashSet<>(events));
        EventCollection savedEventCollection = compilationRepository.save(eventCollection);
        EventCollectionResponse savedEventCollectionResponse = CompilationMapper.toCompilationDto(savedEventCollection);
        return savedEventCollectionResponse;
    }

    @Override
    public EventCollectionResponse patchCompilation(Integer compId, EventCollectionUpdateRequest compilationDto) {
        EventCollection eventCollection = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не найдена подборка id=" + compId));
        if (compilationDto.getCollectionTitle() != null) {
            eventCollection.setCollectionTitle(compilationDto.getCollectionTitle());
        }
        if (compilationDto.getIsPinned() != null) {
            eventCollection.setIsPinned(compilationDto.getIsPinned());
        }
        if (compilationDto.getEventIds() != null) {
            if (!compilationDto.getEventIds().isEmpty()) {
                List<Event> events = eventService.findEventsByIdIn(compilationDto.getEventIds());
                eventCollection.setEvents(new HashSet<>(events));
            }
        }
        EventCollection savedEventCollection = compilationRepository.save(eventCollection);
        EventCollectionResponse savedEventCollectionResponse = CompilationMapper.toCompilationDto(savedEventCollection);
        return savedEventCollectionResponse;
    }

    @Override
    public void deleteCompilation(Integer compId) {
        EventCollection eventCollection = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не найдена подборка id=" + compId));
        eventCollection.getEvents().clear();
        compilationRepository.delete(eventCollection);
    }

    @Override
    public EventCollectionResponse getCompilation(Integer compId) {
        EventCollection eventCollection = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не найдена подборка id=" + compId));
        return CompilationMapper.toCompilationDto(eventCollection);
    }

    @Override
    public List<EventCollectionResponse> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<EventCollection> eventCollections;
        if (pinned != null) {
            eventCollections = compilationRepository.findAllByPinnedEquals(pinned);
        } else {
            eventCollections = compilationRepository.findAll();
        }
        return eventCollections.stream()
                .map(CompilationMapper::toCompilationDto)
                .skip(from)
                .limit(size)
                .toList();
    }
}
