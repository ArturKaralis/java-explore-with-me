package ru.practicum.ewm.main.event.dto.searchrequest;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.main.event.model.EventState;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class PublicSearchParamsDto {

    private String text;
    private Set<Integer> categoriesIds;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private SearchSortOptionDto sortOption;
    private Integer from;
    private Integer size;
    private EventState state;
}