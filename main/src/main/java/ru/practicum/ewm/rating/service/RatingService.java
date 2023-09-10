package ru.practicum.ewm.rating.service;

import ru.practicum.ewm.rating.dto.RatingDto;
import ru.practicum.ewm.rating.model.Rating;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface RatingService {
    List<RatingDto> findAllRatingByUserId(long userId, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    List<RatingDto> findAllRatingByUserIdAndEventId(long userId,
                                                    long eventId,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd);

    void save(long eventId, long userId, Rating rating, HttpServletRequest httpServletRequest);

    void delete(long eventId, long userId, HttpServletRequest httpServletRequest);
}
