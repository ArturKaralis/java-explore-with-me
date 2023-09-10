package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventDtoCreationRequest;
import ru.practicum.ewm.event.dto.EventDtoResponse;
import ru.practicum.ewm.event.dto.EventDtoUpdateRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDtoResponse save(@RequestBody @Validated EventDtoCreationRequest eventDto,
                                 @PathVariable long userId) {
        return service.save(eventDto, userId);
    }

    @GetMapping
    public List<EventDtoResponse> findAllByUserId(@PathVariable long userId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        return service.findAll(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDtoResponse findByUserIdAndById(@PathVariable long userId, @PathVariable long eventId) {
        return service.findById(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestByUserIdAndById(@PathVariable long userId, @PathVariable long eventId) {
        return service.findRequestByUserIdAndById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDtoResponse update(@RequestBody @Validated EventDtoUpdateRequest eventDto,
                                   @PathVariable long userId,
                                   @PathVariable long eventId) {
        return service.update(eventDto, userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public Map<String, List<ParticipationRequestDto>> updateRequests(@RequestBody(required = false) EventRequestStatusUpdateRequest dto,
                                                                     @PathVariable long userId,
                                                                     @PathVariable long eventId) {
        return service.updateRequests(dto, userId, eventId);
    }
}
