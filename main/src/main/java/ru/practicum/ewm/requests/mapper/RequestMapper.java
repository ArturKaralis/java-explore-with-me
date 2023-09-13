package ru.practicum.ewm.requests.mapper;

import ru.practicum.ewm.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.requests.model.ParticipationRequest;

public class RequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getCreated(),
                participationRequest.getEvent().getId(),
                participationRequest.getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus()
        );
    }
}
