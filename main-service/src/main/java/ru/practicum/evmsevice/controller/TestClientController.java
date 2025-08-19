package ru.practicum.evmsevice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evmsevice.client.StatsClient;
import ru.practicum.statdto.HitDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для проверки работы клиента сервера посещений
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class TestClientController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody HitDto dto) {
        log.info("Поступила информация о посещении : " + dto.toString());
        statsClient.post(dto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getStats(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) String uris,
            @RequestParam(defaultValue = "false") Boolean unique,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрашивается информация о посещении эндпоинта {} с {} до {}.", uris, start, end);

        Map<String, Object> parameters = new HashMap<>();
        if (start != null) parameters.put("start", start);
        if (end != null) parameters.put("end", end);
        if (uris != null) parameters.put("uris", uris);
        if (unique != null) parameters.put("unique", unique);
        if (size != null) parameters.put("size", size);
        ResponseEntity<Object> response = statsClient.get(parameters);
        return response;
    }
}

