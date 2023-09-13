package ru.practicum.ewm.stats.server.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.server.mapper.HitMapper;
import ru.practicum.ewm.stats.server.model.Hit;
import ru.practicum.ewm.stats.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsServiceImpl implements StatsService {

    HitRepository hitRepository;

    @Override
    public HitDto addHit(HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        return HitMapper.toHitDto(hitRepository.save(hit));
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong timestamp.");
        }
        if (unique) {
            if (uris != null) {
                return hitRepository.findHitsWithUniqueIpWithUris(uris, start, end);
            }
            return hitRepository.findHitsWithUniqueIpWithoutUris(start, end);
        } else {
            if (uris != null) {
                return hitRepository.findAllHitsWithUris(uris, start, end);
            }
            return hitRepository.findAllHitsWithoutUris(start, end);
        }
    }
}
