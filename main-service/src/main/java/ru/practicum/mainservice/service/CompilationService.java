package ru.practicum.mainservice.service;

import java.util.List;
import ru.practicum.mainservice.dto.CompilationDto;
import ru.practicum.mainservice.dto.NewCompilationDto;
import ru.practicum.mainservice.dto.PatchCompilationDto;

public interface CompilationService {

    List<CompilationDto> findAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto findCompilationById(Integer compId);

    void removeCompilation(Integer compId);

    CompilationDto updateCompilation(Integer compId, PatchCompilationDto compilationDto);

    CompilationDto createCompilation(NewCompilationDto compilationDto);

}
