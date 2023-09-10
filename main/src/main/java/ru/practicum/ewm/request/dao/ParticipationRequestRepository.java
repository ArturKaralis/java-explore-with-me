package ru.practicum.ewm.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.dto.CountRequestDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEvent(Event event);

    long countByEvent(Event event);

    @Query("select pr " +
            "from ParticipationRequest as pr " +
            "where pr.event = :event AND pr.status = :status")
    List<ParticipationRequest> findAllByEventAndStatus(Event event, Status status);

    List<ParticipationRequest> findAllByRequester(User requester);

    @Query("select new CountRequestDto(pr.event.id, count(pr.id)) " +
            "from ParticipationRequest pr " +
            "where pr.event in :events " +
            "group by pr.event")
    List<CountRequestDto> findAllCountRequestByEvents(List<Event> events);
}
