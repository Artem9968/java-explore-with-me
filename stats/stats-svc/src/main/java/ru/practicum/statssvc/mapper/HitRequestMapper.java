package ru.practicum.statssvc.mapper;

import ru.practicum.statsdto.HitDto;
import ru.practicum.statssvc.model.EndpointRequest;

public class HitRequestMapper {

    public static EndpointRequest fromDto(HitDto dto) {
        EndpointRequest endpoint = new EndpointRequest();
        endpoint.setApp(dto.getApp());
        endpoint.setUri(dto.getUri());
        endpoint.setIp(dto.getIp());
        endpoint.setTimestamp(dto.getTimestamp());
        return endpoint;
    }
}
