package ru.practicum.ewm.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    String CREATE_NEW_STAT_DTO = "new ru.practicum.ewm.stats.dto.StatsDto";

    @Query("SELECT " + CREATE_NEW_STAT_DTO + "(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatsDto> findHitsWithUniqueIpWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT " + CREATE_NEW_STAT_DTO + "(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.uri IN (?1) AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatsDto> findHitsWithUniqueIpWithUris(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT " + CREATE_NEW_STAT_DTO + "(h.app, h.uri, COUNT(h.uri)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.uri) DESC")
    List<StatsDto> findAllHitsWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT " + CREATE_NEW_STAT_DTO + "(h.app, h.uri, COUNT(h.uri)) " +
            "FROM Hit AS h " +
            "WHERE h.uri IN (?1) AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.uri) DESC")
    List<StatsDto> findAllHitsWithUris(List<String> uris, LocalDateTime start, LocalDateTime end);
}
