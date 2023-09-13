package ru.practicum.ewm.requests.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.requests.service.RequestService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestController {

    RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getAllRequests(@PathVariable Long userId) {
        log.info("Получаем запрос на список регистрации от Пользователя: userId={}", userId);
        List<ParticipationRequestDto> requestDtoList = requestService.getAllRequests(userId);
        log.info("Возвращаем {} элемент(а/ов).", requestDtoList.size());
        return requestDtoList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Получаем запрос на регистрацию от Пользователя: userId={}, eventId={}", userId, eventId);
        ParticipationRequestDto requestDto = requestService.addRequest(userId, eventId);
        log.info("Возвращаем request={}", requestDto);
        return requestDto;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Получаем запрос на отмену регистрации от Пользователя: userId={}, requestId={}", userId, requestId);
        ParticipationRequestDto requestDto = requestService.cancelRequest(userId, requestId);
        log.info("Возвращаем requestDto={}", requestDto);
        return requestDto;
    }
}
