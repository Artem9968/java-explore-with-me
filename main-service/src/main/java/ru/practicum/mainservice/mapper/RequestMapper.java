package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.model.EventRequest;

public class RequestMapper {
    private RequestMapper() {
    }

    public static RequestDto toRequestDto(EventRequest eventRequest) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(eventRequest.getId());
        requestDto.setCreated(eventRequest.getRequestDate());
        requestDto.setEvent(eventRequest.getEvent().getId());
        requestDto.setRequester(eventRequest.getRequestingUser().getId());
        requestDto.setStatus(eventRequest.getRequestState());
        return requestDto;
    }
}
