package ru.practicum.mainservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import ru.practicum.statsdto.HitDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import ru.practicum.statsclient.StatsClient;

import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonProcessingException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class TestClientController {

    private final StatsClient statsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public void registerHit(@RequestBody HitDto hitDto) {
        log.info("Получен хит: {}", hitDto);
        statsClient.post(hitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public String getStats(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) String uris,
            @RequestParam(defaultValue = "false") Boolean unique,
            @RequestParam(defaultValue = "10") Integer size) {

        log.info("Запрос статистики для эндпоинтов '{}', с {} по {}", uris, start, end);

        Map<String, Object> queryParams = new HashMap<>();
        if (start != null) queryParams.put("start", start);
        if (end != null) queryParams.put("end", end);
        if (uris != null) queryParams.put("uris", uris);
        queryParams.put("unique", unique);
        queryParams.put("size", size);

        ResponseEntity<Object> response = statsClient.get(queryParams);
        if (response == null || response.getBody() == null) {
            log.warn("Сервис статистики вернул пустой ответ");
            return "[]";
        }

        Object responseBody = response.getBody();
        try {
            if (responseBody instanceof String responseString) {
                if (responseString.trim().isEmpty() || responseString.equals("null")) {
                    return "[]";
                }
                return responseString;
            } else {
                return objectMapper.writeValueAsString(responseBody);
            }
        } catch (JsonProcessingException e) {
            log.error("Ошибка преобразования ответа в JSON: {}", e.getMessage());
            return "[]";
        } catch (Exception e) {
            log.error("Ошибка при обработке ответа статистики: {}", e.getMessage());
            return "[]";
        }
    }
}


