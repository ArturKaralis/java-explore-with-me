package ru.practicum.ewm.main.event.dto.searchrequest;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.main.event.model.EventState;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class AdminSearchParamsDto {
    private Set<Long> usersIds;
    private Set<EventState> states;
    private Set<Integer> categoriesIds;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Integer from;
    private Integer size;
}
