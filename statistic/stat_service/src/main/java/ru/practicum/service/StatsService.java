package ru.practicum.service;

import dto.EndpointHitDto;
import dto.ViewStats;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHit create(EndpointHitDto endpointHitDto);

    List<ViewStats> getAll(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
