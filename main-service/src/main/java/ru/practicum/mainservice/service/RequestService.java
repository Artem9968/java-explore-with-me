package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.dto.RequestGroupDto;
import ru.practicum.mainservice.dto.RequestUpdateDto;
import ru.practicum.mainservice.model.EventRequest;

import java.util.List;

public interface RequestService {
    EventRequest createRequest(Integer userId, Integer eventId);

    List<RequestDto> getRequestsByUserId(Integer userId);

    EventRequest canceledRequest(Integer userId, Integer requestId);

    List<EventRequest> getRequestsByEventId(Integer userId, Integer eventId);

    RequestGroupDto updateRequestsStatus(Integer userId, Integer eventId, RequestUpdateDto requestUpdateDto);
}
