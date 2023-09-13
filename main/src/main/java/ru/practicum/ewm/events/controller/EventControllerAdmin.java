package ru.practicum.ewm.events.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventControllerAdmin {

    EventService eventService;

    @GetMapping
    public List<EventFullDto> searchEvents(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<String> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                           @RequestParam(required = false) String rateSort,
                                           @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получаем запрос на получение списка эвентов: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, rateSort={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, rateSort, from, size);
        List<EventFullDto> eventFullDtoList = eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, rateSort, from, size);
        log.info("Возвращаем {} элемент(а/ов)", eventFullDtoList.size());
        return eventFullDtoList;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequest eventAdminRequest) {
        log.info("Получаем запрос на обновление: eventId={}, eventAdminRequest={}", eventId, eventAdminRequest);
        EventFullDto eventFullDto = eventService.updateEventByAdmin(eventId, eventAdminRequest);
        log.info("Возвращаем обновленный eventFullDto={}", eventFullDto);
        return eventFullDto;
    }
}
