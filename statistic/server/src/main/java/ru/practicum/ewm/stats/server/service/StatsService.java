package ru.practicum.ewm.stats.server.service;

import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    HitDto addHit(HitDto hitDto);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
