package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.entity.EndpointHit;
import ru.practicum.entity.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public EndpointHit createHit(EndpointHit endpointHit) {
        return statsRepository.save(endpointHit);
    }

    @Override
    public Collection<ViewStats> getStats(Timestamp start, Timestamp end, List<String> uris, boolean unique) {
        return statsRepository.findStatsByDates(start, end, uris, unique);
    }
}
