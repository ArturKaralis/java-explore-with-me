package ru.practicum.ewm.rating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.rating.dto.RatingDto;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.rating.service.RatingService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateRatingController {
    private final RatingService service;

    @GetMapping("/rating")
    public List<RatingDto> findAllRatingByUserId(@PathVariable long userId,
                                                 @RequestParam(required = false)
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                 @RequestParam(required = false)
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd) {
        return service.findAllRatingByUserId(userId, rangeStart, rangeEnd);
    }

    @GetMapping("/{eventId}/rating")
    public List<RatingDto> findAllRatingByUserIdAndEventId(@PathVariable long userId,
                                                           @PathVariable long eventId,
                                                           @RequestParam(required = false)
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                           @RequestParam(required = false)
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd) {
        return service.findAllRatingByUserIdAndEventId(userId, eventId, rangeStart, rangeEnd);
    }

    @PutMapping("/{eventId}/like")
    public void saveLike(@PathVariable long userId,
                         @PathVariable long eventId,
                         HttpServletRequest httpServletRequest) {
        service.save(eventId, userId, Rating.LIKE, httpServletRequest);
    }

    @PutMapping("/{eventId}/dislike")
    public void saveDislike(@PathVariable long userId,
                            @PathVariable long eventId,
                            HttpServletRequest httpServletRequest) {
        service.save(eventId, userId, Rating.DISLIKE, httpServletRequest);
    }

    @DeleteMapping("/{eventId}/rating")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId,
                       @PathVariable long eventId,
                       HttpServletRequest httpServletRequest) {
        service.delete(eventId, userId, httpServletRequest);
    }
}
