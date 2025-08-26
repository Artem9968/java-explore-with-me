package ru.practicum.mainservice.service;

import java.util.List;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;

public interface CompilationService {

    List<CompilationDto> findAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto findCompilationById(Integer compId);

    void removeCompilation(Integer compId);

    CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest compilationDto);

    CompilationDto createCompilation(NewCompilationDto compilationDto);

}
