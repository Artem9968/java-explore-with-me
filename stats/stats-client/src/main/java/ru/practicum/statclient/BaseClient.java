package ru.practicum.statclient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    private static ResponseEntity<Object> prepareClientResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }

    /**
     * Формируем и отправляем http запрос на сервер
     *
     * @param method     - метод запроса
     * @param path       - эндпоинт
     * @param parameters - карта параметров
     * @param body       - тело запроса
     * @param <T>        - тип объекта тела запроса
     * @return - ResponseEntity
     */
    protected <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method,
                                                            String path,
                                                            Map<String, Object> parameters,
                                                            T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                StringBuilder stringParametrs = new StringBuilder(path);
                stringParametrs.append("?");
                for (String key : parameters.keySet()) {
                    stringParametrs.append(key);
                    stringParametrs.append("={");
                    stringParametrs.append(key);
                    stringParametrs.append("}&");
                }
                serverResponse = rest.exchange(stringParametrs.toString(), method, requestEntity, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareClientResponse(serverResponse);
    }
}
