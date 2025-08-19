package ru.practicum.statsvc.mapper;

import ru.practicum.statdto.HitDto;
import ru.practicum.statsvc.model.EndpointHit;

public class EndpointMapper {
    private EndpointMapper() {
    }

    public static EndpointHit toEndpointHit(HitDto dto) {
        EndpointHit endpoint = new EndpointHit();
        endpoint.setApp(dto.getApp());
        endpoint.setUri(dto.getUri());
        endpoint.setIp(dto.getIp());
        endpoint.setTimestamp(dto.getTimestamp());
        return endpoint;
    }
}
