package ru.practicum.statsvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statdto.HitDto;
import ru.practicum.statdto.StatsDto;
import ru.practicum.statsvc.service.StatService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class StatController {

    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody HitDto dto) {
        log.info("Поступила информация о посещении : " + dto.toString());
        statService.addHit(dto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsDto> getStats(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрашивается информация о посещении эндпоинта {} с {} до {}.", uris, start, end);
        return statService.getStats(start, end, uris, unique, size);
    }
}
