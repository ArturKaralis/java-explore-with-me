package ru.practicum.ewm.compilations.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    List<Long> events;

    Boolean pined;

    @Size(min = 1, max = 50)
    String title;
}
