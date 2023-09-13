package ru.practicum.ewm.events.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventRatedDto;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.service.EventService;
import ru.practicum.ewm.stats.client.hit.HitClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventControllerPublic {

    EventService eventService;

    HitClient hitClient;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(defaultValue = "false") Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                         @RequestParam(required = false) String rateSort,
                                         @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(value = "size", defaultValue = "10") @Positive Integer size,
                                         HttpServletRequest request) {
        log.info("Получаем запрос на получение списка: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, rateSort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, rateSort, from, size);
        request.setAttribute("app_name", "main application");
        log.info("Создаем {} запрос к {} от {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        hitClient.addHit(request);
        List<EventShortDto> eventShortDtoList = eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, rateSort, from, size, request);
        log.info("Возвращаем {} элемент(а/ов)", eventShortDtoList.size());
        return eventShortDtoList;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId,
                                     HttpServletRequest request) {
        log.info("Получаем запрос на получение эвента: eventId={}", eventId);
        request.setAttribute("app_name", "main application");
        log.info("Создаем {} запрос к {} от {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        hitClient.addHit(request);
        EventFullDto eventFullDto = eventService.getEventById(eventId, request);
        log.info("Возвращаем eventFullDto={}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping("/rating")
    public List<EventRatedDto> getRatedEvents(@RequestParam(defaultValue = "HIGH") String rateSort,
                                              @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {

        log.info("Получаем запрос на получение списка рейтинга эвентов: rateSort={} ,from={}, size={}", rateSort, from, size);
        List<EventRatedDto> ratedDtoList = eventService.getRatedEvents(rateSort, from, size);
        log.info("Возвращаем {} элетент(а/ов).", ratedDtoList.size());
        return ratedDtoList;
    }
}
