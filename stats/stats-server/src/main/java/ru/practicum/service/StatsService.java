package ru.practicum.service;

import ru.practicum.entity.EndpointHit;
import ru.practicum.entity.ViewStats;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface StatsService {

    EndpointHit createHit(EndpointHit endpointHit);

    Collection<ViewStats> getStats(Timestamp start, Timestamp end, List<String> uris, boolean unique);

}
