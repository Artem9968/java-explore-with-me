package ru.practicum.statsclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.statsdto.HitDto;
import ru.practicum.statsdto.StatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StatsClient extends BaseClient {

    private final String statsBaseUrl;

    private  final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String statsEndpoint = "/stats";
    private final String eventsPrefix = "/events/";
    private final String hitEndpoint = "/hit";

        @Autowired
    public StatsClient(@Value("${statserver.url}") String statsBaseUrl, RestTemplateBuilder builder) {
        super(builder.build());
        this.statsBaseUrl = statsBaseUrl;
    }

    public void post(HitDto dto) {
        try {
            makeAndSendRequest(HttpMethod.POST, statsBaseUrl + hitEndpoint, null, dto);
        } catch (Exception e) {
            log.error("Ошибка отправки hit в сервис статистики: {}", e.getMessage());
        }
    }

    public ResponseEntity<Object> get(Map<String, Object> parameters) {
        try {
            return makeAndSendRequest(HttpMethod.GET, statsBaseUrl + statsEndpoint, parameters, null);
        } catch (Exception e) {
            log.error("Ошибка получения статистики: {}", e.getMessage());
            return ResponseEntity.ok(List.of());
        }
    }

    public void hitInfo(String appName, String uri, String ip) {
        HitDto hitDto = new HitDto();
        hitDto.setApp(appName);
        hitDto.setUri(uri);
        hitDto.setIp(ip);
        hitDto.setTimestamp(LocalDateTime.now());
        post(hitDto);
    }

    public Integer getEventViews(Integer eventId, Boolean unique) {
        try {
            LocalDateTime start = LocalDateTime.now().minusYears(1); // 1 год назад
            LocalDateTime end = LocalDateTime.now().plusDays(1);     // 1 день вперед

            Map<String, Object> parameters = Map.of(
                    "start", start.format(formatter),
                    "end", end.format(formatter),
                    "uris", eventsPrefix + eventId,
                    "unique", unique
            );

            List<StatsDto> dtos = getList(statsBaseUrl + statsEndpoint, parameters,
                    new ParameterizedTypeReference<List<StatsDto>>() {});

            if (dtos == null || dtos.isEmpty()) {
                return 0;
            }
            return dtos.get(0).getHits();
        } catch (Exception e) {
            log.error("Ошибка получения просмотров для события {}: {}", eventId, e.getMessage());
            return 0;
        }
    }

    public List<StatsDto> getEventViewsByUris(List<String> eventUris, Boolean unique) {
        try {
            if (eventUris == null || eventUris.isEmpty()) {
                return List.of();
            }

            LocalDateTime start = LocalDateTime.now().minusYears(1);
            LocalDateTime end = LocalDateTime.now().plusDays(1);

            Map<String, Object> parameters = Map.of(
                    "start", start.format(formatter),
                    "end", end.format(formatter),
                    "uris", String.join(",", eventUris),
                    "unique", unique
            );

            List<StatsDto> result = getList(statsBaseUrl + statsEndpoint, parameters,
                    new ParameterizedTypeReference<List<StatsDto>>() {});

            return result != null ? result : List.of();
        } catch (Exception e) {
            log.error("Ошибка получения просмотров для URIs {}: {}", eventUris, e.getMessage());
            return List.of();
        }
    }
}
