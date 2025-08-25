package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.model.Request;

public class RequestMapper {
    private RequestMapper() {
    }

    public static RequestDto toRequestDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }
}
