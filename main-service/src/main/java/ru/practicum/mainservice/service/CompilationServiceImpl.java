package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.CompilationDto;
import ru.practicum.mainservice.dto.NewCompilationDto;
import ru.practicum.mainservice.dto.PatchCompilationDto;
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
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        EventCollection eventCollection = CompilationMapper.toCompilation(compilationDto);
        List<Event> events = eventService.findEventsByIdIn(compilationDto.getEvents());
        eventCollection.setEvents(new HashSet<>(events));
        EventCollection savedEventCollection = compilationRepository.save(eventCollection);
        CompilationDto savedCompilationDto = CompilationMapper.toCompilationDto(savedEventCollection);
        return savedCompilationDto;
    }

    @Override
    public CompilationDto patchCompilation(Integer compId, PatchCompilationDto compilationDto) {
        EventCollection eventCollection = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не найдена подборка id=" + compId));
        if (compilationDto.getTitle() != null) {
            eventCollection.setCollectionTitle(compilationDto.getTitle());
        }
        if (compilationDto.getPinned() != null) {
            eventCollection.setIsPinned(compilationDto.getPinned());
        }
        if (compilationDto.getEvents() != null) {
            if (!compilationDto.getEvents().isEmpty()) {
                List<Event> events = eventService.findEventsByIdIn(compilationDto.getEvents());
                eventCollection.setEvents(new HashSet<>(events));
            }
        }
        EventCollection savedEventCollection = compilationRepository.save(eventCollection);
        CompilationDto savedCompilationDto = CompilationMapper.toCompilationDto(savedEventCollection);
        return savedCompilationDto;
    }

    @Override
    public void deleteCompilation(Integer compId) {
        EventCollection eventCollection = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не найдена подборка id=" + compId));
        eventCollection.getEvents().clear();
        compilationRepository.delete(eventCollection);
    }

    @Override
    public CompilationDto getCompilation(Integer compId) {
        EventCollection eventCollection = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не найдена подборка id=" + compId));
        return CompilationMapper.toCompilationDto(eventCollection);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
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
