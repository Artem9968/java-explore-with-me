package ru.practicum.mainservice.service;

import java.util.List;
import ru.practicum.mainservice.dto.RequestDto;
import ru.practicum.mainservice.dto.RequestGroupDto;
import ru.practicum.mainservice.dto.RequestUpdateDto;
import ru.practicum.mainservice.model.Request;

public interface RequestService {

    RequestGroupDto updateRequestStatuses(Integer userId, Integer eventId, RequestUpdateDto requestUpdateDto);

    List<Request> findEventRequests(Integer userId, Integer eventId);

    Request cancelRequest(Integer userId, Integer requestId);

    List<RequestDto> findUserRequests(Integer userId);

    Request createRequest(Integer userId, Integer eventId);
}
