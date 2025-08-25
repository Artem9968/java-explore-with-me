package ru.practicum.mainservice.service;


import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.dto.RequestGroupDto;
import ru.practicum.mainservice.model.Request;

import java.util.List;

public interface RequestService {
    Request createRequest(Integer userId, Integer eventId);

    List<RequestDto> getRequestsByUserId(Integer userId);

    Request canceledRequest(Integer userId, Integer requestId);

    List<Request> getRequestsByEventId(Integer userId, Integer eventId);

    RequestGroupDto updateRequestsStatus(Integer userId, Integer eventId, RequestUpdateDto requestUpdateDto);
}
