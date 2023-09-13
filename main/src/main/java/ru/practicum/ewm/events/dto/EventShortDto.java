package ru.practicum.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.users.dto.UserShortDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    @NotBlank
    String annotation;

    CategoryDto category;

    Integer confirmedRequests;

    @NotBlank
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    Long id;

    @NotNull
    UserShortDto initiator;

    @NotNull
    Boolean paid;

    @NotBlank
    String title;

    Long views;
}
