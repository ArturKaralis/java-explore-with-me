package ru.practicum.ewm.rating.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.rating.dto.RatingDto;
import ru.practicum.ewm.statistics.dto.RequestHitDto;

import java.time.LocalDateTime;

@UtilityClass
public class RatingMapper {

    public RatingDto mapToRatingDto(RequestHitDto ratingStat) {
        String[] uri = ratingStat.getUri().split("/");
        return mapToRatingDto(uri[5], Long.parseLong(uri[4]), ratingStat.getTimestamp());
    }

    public RatingDto mapToRatingDto(String rating, long eventId, LocalDateTime timestamp) {
        return RatingDto
                .builder()
                .rating(rating)
                .eventId(eventId)
                .timestamp(timestamp)
                .build();
    }
}
