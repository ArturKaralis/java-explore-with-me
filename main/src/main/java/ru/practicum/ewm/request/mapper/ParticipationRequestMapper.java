package ru.practicum.ewm.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ParticipationRequestMapper {
    public ParticipationRequest mapToParticipationRequest(Event event, User requester, Status status, LocalDateTime created) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setCreated(created);
        participationRequest.setEvent(event);
        participationRequest.setRequester(requester);
        participationRequest.setStatus(status);
        return participationRequest;
    }

    public List<ParticipationRequestDto> mapToParticipantRequestDto(List<ParticipationRequest> requests) {
        return requests
                .stream()
                .map(ParticipationRequestMapper::mapToParticipantRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto mapToParticipantRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto
                .builder()
                .created(participationRequest.getCreated())
                .event(participationRequest.getEvent().getId())
                .id(participationRequest.getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus())
                .build();
    }
}
