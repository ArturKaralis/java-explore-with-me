package ru.practicum.ewm.events.service;

import ru.practicum.ewm.events.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventFullByOwner(Long userId, Long eventId);

    List<EventShortDto> getEventsShortByOwner(Long userId, Integer from, Integer size);

    EventFullDto updateEventByOwner(Long userId, Long eventId, UpdateEventUserRequest eventUserRequest);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventAdminRequest);

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                  Boolean onlyAvailable, String sort, String rateSort, Integer from, Integer size,
                                  HttpServletRequest request);

    List<EventFullDto> searchEvents(List<Long> users, List<String> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, String rateSort,
                                    Integer from, Integer size);

    EventFullDto getEventById(Long eventId, HttpServletRequest request);

    List<EventRatedDto> getRatedEvents(String rateSort, Integer from, Integer size);
}
