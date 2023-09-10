package ru.practicum.ewm.rating.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.rating.dao.RatingRepository;
import ru.practicum.ewm.rating.dto.RatingDto;
import ru.practicum.ewm.rating.mapper.RatingMapper;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.rating.model.RatingEvent;
import ru.practicum.ewm.rating.model.RatingEventKey;
import ru.practicum.ewm.statistics.client.client.StatisticsClient;
import ru.practicum.ewm.statistics.dto.RequestHitDto;
import ru.practicum.ewm.statistics.dto.ResponseHitDto;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.exception.IncorrectIdException;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.practicum.ewm.event.model.State.PUBLISHED;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository repo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    private final StatisticsClient client;
    private final ObjectMapper objectMapper;

    @Value("${ewm-app.name}")
    private String app;

    @Override
    public List<RatingDto> findAllRatingByUserId(long userId, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        userRepo.findById(userId).orElseThrow(() -> new IncorrectIdException(userId, "user"));

        try {
            List<RequestHitDto> ratings = objectMapper.readValue(
                    client.rating(rangeStart, rangeEnd, null, String.format("/users/%d", userId)).getBody().toString(),
                    new TypeReference<>(){});
            return Collections.singletonList(RatingMapper.mapToRatingDto((RequestHitDto) ratings));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RatingDto> findAllRatingByUserIdAndEventId(long userId, long eventId, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        userRepo.findById(userId).orElseThrow(() -> new IncorrectIdException(userId, "user"));
        eventRepo.findByIdAndState(eventId, PUBLISHED).orElseThrow(() -> new IncorrectIdException(eventId, "event"));

        List<String> uris = List.of(
                String.format("/users/%d/events/%d/like", userId, eventId),
                String.format("/users/%d/events/%d/dislike", userId, eventId),
                String.format("/users/%d/events/%d/rating", userId, eventId));
        try {
            List<ResponseHitDto> ratings = objectMapper.readValue(
                    client.rating(rangeStart, rangeEnd, uris, null).getBody().toString(),
                    new TypeReference<>(){});
            return Collections.singletonList(RatingMapper.mapToRatingDto((RequestHitDto) ratings));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void save(long eventId, long userId, Rating rating, HttpServletRequest httpServletRequest) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IncorrectIdException(userId, "user"));
        Event event = eventRepo.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> new IncorrectIdException(eventId, "event"));
        repo.saveAndFlush(new RatingEvent(new RatingEventKey(userId, eventId), rating, user, event));
        client.hit(app, httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
    }

    @Transactional
    @Override
    public void delete(long eventId, long userId, HttpServletRequest httpServletRequest) {
        repo.deleteByEventAndUser(
                eventRepo.findByIdAndState(eventId, PUBLISHED)
                        .orElseThrow(() -> new IncorrectIdException(eventId, "event")),
                userRepo.findById(userId)
                        .orElseThrow(() -> new IncorrectIdException(userId, "user")));
        client.hit(app, httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
    }
}
