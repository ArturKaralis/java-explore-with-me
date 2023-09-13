package ru.practicum.ewm.requests.service;

import ru.practicum.ewm.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.requests.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.requests.model.EventRequestStatusUpdateResult;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByOwnerEvent(Long userId, Long eventId);

    List<ParticipationRequestDto> getAllRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    EventRequestStatusUpdateResult updateStatusRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}
