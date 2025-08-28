package ru.practicum.statsclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    private static ResponseEntity<Object> prepareClientResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        log.warn("Сервис статистики вернул ошибку: {}", response.getStatusCode());
        return ResponseEntity.ok(Collections.emptyList());
    }

    protected <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method,
                                                            String fullUrl,
                                                            Map<String, Object> parameters,
                                                            T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        try {
            URI uri = buildUri(fullUrl, parameters);
            ResponseEntity<Object> serverResponse = rest.exchange(uri, method, requestEntity, Object.class);
            return prepareClientResponse(serverResponse);
        } catch (HttpStatusCodeException e) {
            log.error("Ошибка сервиса статистики {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.ok(Collections.emptyList());
        } catch (Exception e) {
            log.error("Ошибка связи с сервисом статистики: {}", e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    protected <T> List<T> getList(String fullUrl, Map<String, Object> parameters,
                                  ParameterizedTypeReference<List<T>> typeReference) {
        try {
            URI uri = buildUri(fullUrl, parameters);
            ResponseEntity<List<T>> response = rest.exchange(uri, HttpMethod.GET, null, typeReference);

            if (response.getStatusCode().is2xxSuccessful()) {
                List<T> body = response.getBody();
                if (body != null) {
                    return body;
                } else {
                    log.warn("Сервис статистики вернул пустой ответ для URL: {}", fullUrl);
                    return Collections.emptyList();
                }
            } else {
                log.warn("Сервис статистики вернул ошибку {} для URL: {}", response.getStatusCode(), fullUrl);
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("Ошибка получения списка из сервиса статистики для URL {}: {}", fullUrl, e.getMessage());
            return Collections.emptyList();
        }
    }

    private URI buildUri(String fullUrl, Map<String, Object> parameters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fullUrl);

        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        return builder.build().encode().toUri();
    }
}
