package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.ParticipationRequestResponse;
import ru.practicum.mainservice.model.EventRequest;

public class RequestMapper {
    private RequestMapper() {
    }

    public static ParticipationRequestResponse toRequestDto(EventRequest eventRequest) {
        ParticipationRequestResponse participationRequestResponse = new ParticipationRequestResponse();
        participationRequestResponse.setId(eventRequest.getId());
        participationRequestResponse.setCreationDate(eventRequest.getRequestDate());
        participationRequestResponse.setEventId(eventRequest.getEvent().getId());
        participationRequestResponse.setRequesterId(eventRequest.getRequestingUser().getId());
        participationRequestResponse.setStatus(eventRequest.getRequestState());
        return participationRequestResponse;
    }
}
