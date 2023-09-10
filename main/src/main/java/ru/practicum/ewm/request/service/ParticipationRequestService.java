package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto save(long userId, long eventId);

    List<ParticipationRequestDto> findByUserId(long userId);

    ParticipationRequestDto cancel(long userId, long requestId);
}
