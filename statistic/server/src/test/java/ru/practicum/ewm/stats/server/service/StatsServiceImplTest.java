package ru.practicum.ewm.stats.server.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.server.mapper.HitMapper;
import ru.practicum.ewm.stats.server.model.Hit;
import ru.practicum.ewm.stats.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class StatsServiceImplTest {

    @Mock
    HitRepository hitRepository;

    @InjectMocks
    StatsServiceImpl statsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        statsService = new StatsServiceImpl(hitRepository);
    }

    @Test
    void addHit() {
        //Создаем сущность
        Long id = 1L;
        String app = "ewm-main-service";
        String uri = "/events/1";
        String ip = "192.163.0.1";
        LocalDateTime timestamp = LocalDateTime.of(2022, 9, 6, 11, 0, 23);

        Hit hit = new Hit();
        hit.setId(id);
        hit.setApp(app);
        hit.setUri(uri);
        hit.setIp(ip);
        hit.setTimestamp(timestamp);
        HitDto hitDto = HitMapper.toHitDto(hit);

        when(hitRepository.save(any(Hit.class))).thenReturn(hit);

        //Тестируем
        HitDto result = statsService.addHit(hitDto);

        //Делаем проверки
        assertNotNull(result);
        assertEquals(HitMapper.toHitDto(hit), result);
        verify(hitRepository, times(1)).save(any(Hit.class));
    }

    @Test
    void getStats_WithUniqueIp_WithUris() {
        //Создаем сущность
        String app = "ewm-main-service";
        String uri = "/events/1";
        Long hits = 6L;

        LocalDateTime start = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
        List<String> uris = new ArrayList<>();
        uris.add(uri);
        Boolean unique = true;
        StatsDto statsDto = new StatsDto();
        statsDto.setApp(app);
        statsDto.setUri(uri);
        statsDto.setHits(hits);
        List<StatsDto> statsDtoList = new ArrayList<>();
        statsDtoList.add(statsDto);

        when(hitRepository.findHitsWithUniqueIpWithUris(any(List.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(statsDtoList);

        //Тестируем
        List<StatsDto> result = statsService.getStats(start, end, uris, unique);

        //Проверяем
        assertNotNull(result);
        assertEquals(statsDtoList, result);
        verify(hitRepository, times(1)).findHitsWithUniqueIpWithUris(uris, start, end);
    }

    @Test
    void getStats_WithUniqueIp_WithoutUris() {
        //Создаем сущность
        String app = "ewm-main-service";
        String uri = "/events/1";
        Long hits = 6L;

        LocalDateTime start = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
        Boolean unique = true;
        StatsDto statsDto = new StatsDto(app, uri, hits);
        List<StatsDto> statsDtoList = new ArrayList<>();

        when(hitRepository.findHitsWithUniqueIpWithoutUris(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(statsDtoList);

        //Тестируем
        List<StatsDto> result = statsService.getStats(start, end, null, unique);

        //Проверяем
        assertNotNull(result);
        assertEquals(statsDtoList, result);
        verify(hitRepository, times(1)).findHitsWithUniqueIpWithoutUris(start, end);
    }

    @Test
    void getStats_AllHits_WithUris() {
        //Создаем сущность
        String app = "ewm-main-service";
        String uri = "/events/1";
        Long hits = 6L;

        LocalDateTime start = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
        List<String> uris = new ArrayList<>();
        uris.add(uri);
        Boolean unique = false;
        StatsDto statsDto = new StatsDto(app, uri, hits);
        List<StatsDto> statsDtoList = new ArrayList<>();

        when(hitRepository.findAllHitsWithUris(any(List.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(statsDtoList);

        //Тестируем
        List<StatsDto> result = statsService.getStats(start, end, uris, unique);

        //Проверяем
        assertNotNull(result);
        assertEquals(statsDtoList, result);
        verify(hitRepository, times(1)).findAllHitsWithUris(uris, start, end);
    }

    @Test
    void getStats_AllHits_WithoutUris() {
        //Создаем сущность
        String app = "ewm-main-service";
        String uri = "/events/1";
        Long hits = 6L;

        LocalDateTime start = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
        Boolean unique = false;
        StatsDto statsDto = new StatsDto(app, uri, hits);
        List<StatsDto> statsDtoList = new ArrayList<>();

        when(hitRepository.findAllHitsWithoutUris(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(statsDtoList);

        //Тестируем
        List<StatsDto> result = statsService.getStats(start, end, null, unique);

        //Проверяем
        assertNotNull(result);
        assertEquals(statsDtoList, result);
        verify(hitRepository, times(1)).findAllHitsWithoutUris(start, end);
    }

    @Test
    void getStats_Response_Bad_Request() {
        //Создаем сущность
        String app = "ewm-main-service";
        String uri = "/events/1";
        Long hits = 6L;

        LocalDateTime start = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
        List<String> uris = new ArrayList<>();
        uris.add(uri);
        Boolean unique = false;
        StatsDto statsDto = new StatsDto(app, uri, hits);
        List<StatsDto> statsDtoList = new ArrayList<>();
        statsDtoList.add(statsDto);

        //Тестируем и проверяем
        assertThrows(ResponseStatusException.class, () -> statsService.getStats(start, end, uris, unique));
    }
}