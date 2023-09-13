package ru.practicum.ewm.compilations.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.events.dto.EventShortDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    List<EventShortDto> events;

    Long id;

    Boolean pinned;

    String title;
}
