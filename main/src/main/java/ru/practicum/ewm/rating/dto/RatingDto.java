package ru.practicum.ewm.rating.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingDto {
    Long id;

    Long userId;

    Long eventId;

    Boolean likes;
}
