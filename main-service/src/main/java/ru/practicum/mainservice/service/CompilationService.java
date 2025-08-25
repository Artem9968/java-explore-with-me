package ru.practicum.mainservice.service;


import ru.practicum.mainservice.dto.CompilationDto;
import ru.practicum.mainservice.dto.NewCompilationDto;
import ru.practicum.mainservice.dto.PatchCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto compilationDto);

    CompilationDto patchCompilation(Integer compId, PatchCompilationDto compilationDto);

    void deleteCompilation(Integer compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Integer compId);
}
