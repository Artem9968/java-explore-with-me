package ru.practicum.mainservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statsclient.StatsClient;
import ru.practicum.statsdto.HitDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class TestClientController {
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody HitDto dto) {
        log.info("Поступила информация о посещении: {}", dto.toString());
        statsClient.post(dto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public String getStats(
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

        // ФИКС: Правильная обработка ответа
        if (response == null) {
            log.warn("Stats client returned null response");
            return "[]";
        }

        Object responseBody = response.getBody();
        if (responseBody == null) {
            log.warn("Stats service returned null body");
            return "[]";
        }

        try {
            // Преобразуем ответ в правильный JSON
            if (responseBody instanceof String) {
                // Если это уже строка, проверяем что это валидный JSON
                String bodyString = (String) responseBody;
                if (bodyString.trim().isEmpty() || bodyString.equals("null")) {
                    return "[]";
                }
                return bodyString;
            } else {
                // Если это объект, сериализуем в JSON
                return objectMapper.writeValueAsString(responseBody);
            }
        } catch (JsonProcessingException e) {
            log.error("Error converting response to JSON: {}", e.getMessage());
            return "[]";
        } catch (Exception e) {
            log.error("Unexpected error processing stats response: {}", e.getMessage());
            return "[]";
        }
    }
}

