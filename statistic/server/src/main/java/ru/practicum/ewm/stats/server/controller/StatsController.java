package ru.practicum.ewm.stats.server.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsController {

    StatsService statsService;

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получаем запрос: start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        List<StatsDto> stats = statsService.getStats(start, end, uris, unique);
        log.info("Возвращаем {} элемент(а/ов).", stats.size());
        return stats;
    }

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public HitDto addHit(@RequestBody @Valid HitDto hitDto) {
        log.info("Получаем запрос hitDto={}", hitDto);
        HitDto newHitDto = statsService.addHit(hitDto);
        log.info("Возвращаем ответ hitDto={}", newHitDto);
        return newHitDto;
    }
}
