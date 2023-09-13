package ru.practicum.ewm.rating.mapper;

import ru.practicum.ewm.rating.dto.RatingDto;
import ru.practicum.ewm.rating.model.Rating;

public class RatingMapper {
    public static Rating toRating(RatingDto ratingDto) {
        return Rating.builder()
                .id(ratingDto.getId())
                .userId(ratingDto.getUserId())
                .eventId(ratingDto.getEventId())
                .likes(ratingDto.getLikes())
                .build();
    }

    public static RatingDto toRatingDto(Rating rating) {
        return RatingDto.builder()
                .id(rating.getId())
                .userId(rating.getUserId())
                .eventId(rating.getEventId())
                .likes(rating.getLikes())
                .build();
    }
}
