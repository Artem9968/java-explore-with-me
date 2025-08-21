package ru.practicum.statsclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statsdto.HitDto;

import java.util.Map;

@Component
public class StatsClient extends BaseClient {
    private final String hitEndpoint = "/hit";
    private final String statsEndpoint = "/stats";

    @Autowired
    public StatsClient(@Value("${statserver.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public void post(HitDto dto) {
        makeAndSendRequest(HttpMethod.POST, hitEndpoint, null, dto);
    }

    public ResponseEntity<Object> get(Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, statsEndpoint, parameters, null);
    }
}
