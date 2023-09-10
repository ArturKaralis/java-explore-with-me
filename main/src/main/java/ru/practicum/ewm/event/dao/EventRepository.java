package ru.practicum.ewm.event.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator(User initiator, Pageable pageable);

    @Query("select e " +
            "from  Event e " +
            "where (coalesce(:state, null) is null or e.state = :state) " +
            "and ((coalesce(:text, null) is null or lower(e.annotation) like %:text%) " +
            "or (coalesce(:text, null) is null or lower(e.description) like %:text%)) " +
            "and (coalesce(:categories, null) is null or e.category.id in :categories) " +
            "and (coalesce(:paid, null) is null or e.paid in :paid) " +
            "and (coalesce(:rangeStart, null) is null or e.eventDate >= :rangeStart) " +
            "and (coalesce(:rangeEnd, null) is null or e.eventDate <= :rangeEnd)")
    List<Event> findAll(State state,
                        String text,
                        List<Long> categories,
                        Boolean paid,
                        LocalDateTime rangeStart,
                        LocalDateTime rangeEnd,
                        Pageable pageable);

    @Query("select e " +
            "from Event e " +
            "where (coalesce(:users, null) is null or e.initiator.id in :users) " +
            "and (coalesce(:states, null) is null or e.state in :states) " +
            "and (coalesce(:categories, null) is null or e.category.id in :categories) " +
            "and (coalesce(:rangeStart, null) is null or e.eventDate >= :rangeStart) " +
            "and (coalesce(:rangeEnd, null) is null or e.eventDate <= :rangeEnd)")
    List<Event> findAll(List<Long> users,
                        List<State> states,
                        List<Long> categories,
                        LocalDateTime rangeStart,
                        LocalDateTime rangeEnd,
                        Pageable pageable);

    Event findByIdAndInitiator(long id, User initiator);

    Optional<Event> findByIdAndState(long id, State state);
}
