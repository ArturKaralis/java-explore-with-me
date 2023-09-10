package ru.practicum.ewm.event.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDtoCreationRequest {
    @NotBlank
    @Size(max = 2000, min = 20)
    String annotation;
    @NotNull
    @Positive
    Long category;
    @NotBlank
    @Size(max = 7000, min = 20)
    String description;
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    @Valid
    LocationDto location;
    boolean paid;
    @PositiveOrZero
    int participantLimit;
    Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
}
