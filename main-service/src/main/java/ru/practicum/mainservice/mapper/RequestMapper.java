package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.model.Request;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        RequestDto result = new RequestDto();
        result.setId(request.getId());
        result.setCreated(request.getCreated());
        result.setEvent(request.getEvent().getId());
        result.setRequester(request.getRequester().getId());
        result.setStatus(request.getStatus());
        return result;
    }

}


