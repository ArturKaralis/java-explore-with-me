package ru.practicum.ewm.main.event.repository;

import java.util.List;
import java.util.Map;

public interface RateRepository {


    Map<Long, Long> getRatingsForEvents(List<Long> eventsIds);

    void deleteRate(Long userId, Long eventId, int rate);

    void addRate(Long userId, Long eventId, int rate);

    Long getRatingForEvent(Long eventId);

}