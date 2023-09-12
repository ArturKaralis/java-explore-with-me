package ru.practicum.ewm.statistics.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.statistics.dto.RequestHitDto;
import ru.practicum.ewm.statistics.dto.ResponseHitDto;
import ru.practicum.ewm.statistics.server.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.util.constant.Constants.DATE_TIME_FORMAT;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody RequestHitDto hitDto) {
        service.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<ResponseHitDto> getStats(@RequestParam(name = "start") @DateTimeFormat(fallbackPatterns = DATE_TIME_FORMAT) LocalDateTime start,
                                         @RequestParam(name = "end") @DateTimeFormat(fallbackPatterns = DATE_TIME_FORMAT) LocalDateTime end,
                                         @RequestParam(name = "uris", required = false) List<String> uris,
                                         @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
