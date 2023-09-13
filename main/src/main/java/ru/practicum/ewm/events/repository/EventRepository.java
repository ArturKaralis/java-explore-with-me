package ru.practicum.ewm.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.events.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

    Page<Event> findAllByRateIsNotNull(Pageable pageable);
}
