package ru.practicum.statssvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsdto.HitDto;
import ru.practicum.statsdto.StatsDto;
import ru.practicum.statssvc.exception.ValidationException;
import ru.practicum.statssvc.service.StatsService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerHit(@RequestBody HitDto hitData) {
        log.info("Новый запрос на сохранение посещения: {}", hitData);
        statsService.registerHit(hitData);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsDto> getStatistics(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique,
            @RequestParam(defaultValue = "10") Integer size) {

        checkRequiredParams(start, end);
        log.info("Запрос статистики: период с {} по {}, URIs: {}", start, end, uris);

        return statsService.getStatistics(start, end, uris, unique, size);
    }

    private void checkRequiredParams(String start, String end) {
        if (start == null || start.isBlank()) {
            throw new ValidationException("Не указано время начала периода");
        }
        if (end == null || end.isBlank()) {
            throw new ValidationException("Не указано время окончания периода");
        }
    }
}
