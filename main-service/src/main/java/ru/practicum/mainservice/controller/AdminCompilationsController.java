package ru.practicum.mainservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.EventCollectionResponse;
import ru.practicum.mainservice.dto.EventCollectionCreateRequest;
import ru.practicum.mainservice.dto.EventCollectionUpdateRequest;
import ru.practicum.mainservice.service.CompilationService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final CompilationService compilationService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EventCollectionResponse createCompilation(@Validated @RequestBody EventCollectionCreateRequest eventCollectionCreateRequest) {
        log.info("Администратор создает подборку событий '{}'.", eventCollectionCreateRequest.getCollectionTitle());
        return compilationService.createCompilation(eventCollectionCreateRequest);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public EventCollectionResponse updateCompilation(@PathVariable Integer compId,
                                                     @Validated @RequestBody EventCollectionUpdateRequest compilationDto) {
        log.info("Администратор обновляет подборку событий '{}'.", compilationDto.getCollectionTitle());
        return compilationService.patchCompilation(compId, compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        log.info("Администратор удаляет подборку событий id={}.", compId);
        compilationService.deleteCompilation(compId);
    }
}
