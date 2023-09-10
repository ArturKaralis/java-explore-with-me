package ru.practicum.ewm.rating.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.rating.dto.CountRatingDto;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.rating.model.RatingEvent;
import ru.practicum.ewm.rating.model.RatingEventKey;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface RatingRepository extends JpaRepository<RatingEvent, RatingEventKey> {
    long countByEventAndRating(Event event, Rating rating);

    @Query("select new CountRatingDto(re.event.id, count(re.rating)) " +
            "from RatingEvent re " +
            "where re.event in :events and re.rating = :rating " +
            "group by re.event")
    List<CountRatingDto> findAllCountRatingByEventsAndRating(List<Event> events, Rating rating);

    void deleteByEventAndUser(Event event, User user);
}
