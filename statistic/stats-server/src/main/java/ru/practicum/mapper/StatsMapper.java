package ru.practicum.mapper;

import ru.practicum.dto.EndpointHitRequestDto;
import ru.practicum.dto.EndpointHitResponseDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.entity.EndpointHit;
import ru.practicum.entity.ViewStats;

import java.util.Collection;
import java.util.stream.Collectors;

public class StatsMapper {

    public static EndpointHit toEndpointHit(EndpointHitRequestDto dto) {

        return EndpointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public static EndpointHitResponseDto toEndpointHitResponseDto(EndpointHit entity) {
        return EndpointHitResponseDto.builder()
                .id(entity.getId())
                .app(entity.getApp())
                .uri(entity.getUri())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public static Collection<ViewStatsResponseDto> toCollectionViewStatsResponseDto(
            Collection<ViewStats> viewStatsCollection) {
        return viewStatsCollection.stream()
                .map(StatsMapper::toViewStatsResponseDto)
                .collect(Collectors.toList());
    }

    private static ViewStatsResponseDto toViewStatsResponseDto(ViewStats viewStats) {
        return ViewStatsResponseDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }

}
