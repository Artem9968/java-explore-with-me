package ru.practicum.mainservice.service.compilation;


import ru.practicum.mainservice.storage.compilation.CompilationRepository;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.mapper.compilation.CompilationMapper;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.model.compilation.Compilation;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.service.event.EventService;

import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final EventService eventService;
    private final CompilationRepository compilationRepository;

    private Compilation findCompilationOrThrow(Integer compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не существует"));
    }

    private List<Compilation> getCompilationsBasedOnPinned(Boolean pinned) {
        return pinned != null ?
                compilationRepository.findByPinned(pinned) :
                compilationRepository.findAll();
    }

    private void updateCompilationFields(Compilation compilation, UpdateCompilationRequest dto) {
        if (dto.getTitle() != null) {
            compilation.setTitle(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            updateCompilationEvents(compilation, dto.getEvents());
        }
    }

    private void updateCompilationEvents(Compilation compilation, List<Integer> eventIds) {
        List<Event> events = eventService.findEventsByIdIn(eventIds);
        compilation.setEvents(new HashSet<>(events));
    }

    @Override
    public List<CompilationDto> findAllCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = getCompilationsBasedOnPinned(pinned);
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .skip(from)
                .limit(size)
                .toList();
    }

    @Override
    public CompilationDto findCompilationById(Integer compId) {
        Compilation compilation = findCompilationOrThrow(compId);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void removeCompilation(Integer compId) {
        Compilation compilation = findCompilationOrThrow(compId);
        compilation.getEvents().clear();
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest compilationDto) {
        Compilation compilation = findCompilationOrThrow(compId);
        updateCompilationFields(compilation, compilationDto);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        List<Event> events = eventService.findEventsByIdIn(compilationDto.getEvents());
        compilation.setEvents(new HashSet<>(events));
        Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(savedCompilation);
    }
}