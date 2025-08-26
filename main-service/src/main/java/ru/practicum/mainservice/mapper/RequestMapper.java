package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.model.request.ParticipationRequest;

public class RequestMapper {

    public static ParticipationRequestDto toRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto result = new ParticipationRequestDto();
        result.setId(participationRequest.getId());
        result.setCreated(participationRequest.getCreated());
        result.setEvent(participationRequest.getEvent().getId());
        result.setRequester(participationRequest.getRequester().getId());
        result.setStatus(participationRequest.getStatus());
        return result;
    }

}


