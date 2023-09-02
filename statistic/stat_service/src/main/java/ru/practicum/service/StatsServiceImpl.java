package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.converter.EndpointHitConverter;
import dto.EndpointHitDto;
import dto.ViewStats;
import ru.practicum.model.App;
import ru.practicum.model.EndpointHit;
import ru.practicum.storage.AppStorage;
import ru.practicum.storage.StatsStorage;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsStorage statsStorage;
    private final AppStorage appStorage;

    @Override
    @Transactional
    public EndpointHit create(EndpointHitDto endpointHitDto) {
        log.info("Start method create in StatsServiceImpl with endpointHitDto={}", endpointHitDto);
        App app = appStorage.findByName(endpointHitDto.getApp())
                        .orElseGet(() -> appStorage.save(new App(null, endpointHitDto.getApp())));
        EndpointHit endpointHit = statsStorage.save(EndpointHitConverter.toEndpointHit(endpointHitDto, app));
        return endpointHit;
    }

    @Override
    public List<ViewStats> getAll(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Start method getAll in StatsServiceImpl with start={}, end={}, " +
                "uris={}, unique={}", start, end, uris, unique);

        List<ViewStats> stats;

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                stats = statsStorage.findAllIfNoUrisUnique(start, end);
            } else {
                stats = statsStorage.findAllIfNoUris(start, end);
            }
        } else {
            if (unique) {
                stats = statsStorage.findAllUniqueIp(start, end, uris);
            } else {
                stats = statsStorage.findAll(start, end, uris);
            }
        }

        return stats;
    }
}
