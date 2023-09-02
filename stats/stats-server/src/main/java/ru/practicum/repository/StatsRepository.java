package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.entity.EndpointHit;
import ru.practicum.entity.ViewStats;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.entity.ViewStats(e.app, e.uri, " +
            "(CASE when :unique=true then COUNT(DISTINCT e.ip) else COUNT(e.ip) end) " +
            ") " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    Collection<ViewStats> findStatsByDates(Timestamp start,
                                           Timestamp end,
                                           List<String> uris,
                                           boolean unique);
}
