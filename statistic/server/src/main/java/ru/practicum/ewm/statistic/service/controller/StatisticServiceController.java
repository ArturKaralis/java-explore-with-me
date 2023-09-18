package ru.practicum.ewm.statistic.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.statistic.dto.EndpointHitDto;
import ru.practicum.ewm.statistic.dto.ViewStatsDto;
import ru.practicum.ewm.statistic.service.service.StatisticService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.util.constant.Constants.DATE_TIME_FORMAT;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class StatisticServiceController {

    private final StatisticService statisticService;

    @PostMapping("/hit")
    public ResponseEntity<Void> saveEndpointHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("Start POST /hit with {}", endpointHitDto);
        statisticService.saveEndpointHit(endpointHitDto);
        log.info("Finish POST /hit with {}", endpointHitDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getViewStats(
            @RequestParam(name = "start", required = true)
            @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
            @RequestParam(name = "end", required = true)
            @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", required = false, defaultValue = "false") boolean unique
    ) {
        log.info("Start GET/stats with start: {}, end: {}, uris: {}, unique: {}",
                start, end, uris, unique);
        List<ViewStatsDto> stats = statisticService.getViewStats(start, end, uris, unique);
        log.info("Finish GET/stats with {}", stats);
        return stats;
    }

}
