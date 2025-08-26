package ru.practicum.mainservice.service;

import java.util.List;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.model.request.ParticipationRequest;

public interface RequestService {

    EventRequestStatusUpdateResult updateRequestStatuses(Integer userId, Integer eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<ParticipationRequest> findEventRequests(Integer userId, Integer eventId);

    ParticipationRequest cancelRequest(Integer userId, Integer requestId);

    List<ParticipationRequestDto> findUserRequests(Integer userId);

    ParticipationRequest createRequest(Integer userId, Integer eventId);
}
