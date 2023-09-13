package ru.practicum.ewm.rating.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.repository.EventRepository;
import ru.practicum.ewm.rating.dto.RatingDto;
import ru.practicum.ewm.rating.mapper.RatingMapper;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.rating.repository.RatingRepository;
import ru.practicum.ewm.requests.enums.RequestStatus;
import ru.practicum.ewm.requests.model.ParticipationRequest;
import ru.practicum.ewm.requests.repository.RequestRepository;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingServiceImpl implements RatingService {

    RatingRepository ratingRepository;

    UserRepository userRepository;

    EventRepository eventRepository;

    RequestRepository requestRepository;

    @Override
    public RatingDto addRating(Long userId, Long eventId, Boolean likes) {
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found."));
        Rating existRating = ratingRepository.findByUserIdAndEventId(userId, eventId).orElse(null);
        User initiator = event.getInitiator();
        if (existRating != null && likes != null) {
            return updateRating(initiator, event, existRating, likes);
        } else if (likes == null) {
            if (existRating != null) {
                deleteRating(initiator, event, existRating);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating not found.");
            }
        } else {
            if (ratingRepository.existsByUserIdAndEventId(userId, eventId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You already rate this event");
            }
            if (initiator.getId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You can't rate your event.");
            }
            ParticipationRequest request = requestRepository.findByRequesterIdAndEventId(userId, eventId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found."));
            if (!request.getStatus().equals(RequestStatus.CONFIRMED)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You can't rate, because, you wasn't be there.");
            }
            Rating rating = Rating.builder()
                    .userId(userId)
                    .eventId(eventId)
                    .likes(likes)
                    .build();
            if (rating.getLikes()) {
                event.setLikes(event.getLikes() != null ? (event.getLikes() + 1) : 1);
                initiator.setLikes(initiator.getLikes() != null ? (initiator.getLikes() + 1) : 1);
            } else {
                event.setDislikes(event.getDislikes() != null ? event.getDislikes() + 1 : 1);
                initiator.setDislikes(initiator.getDislikes() != null ? initiator.getDislikes() + 1 : 1);
            }
            setRate(event, initiator);
            return RatingMapper.toRatingDto(ratingRepository.save(rating));
        }
        return null;
    }

    private RatingDto updateRating(User initiator, Event event, Rating rating, Boolean likes) {
        if (rating.getLikes().equals(likes)) {
            return RatingMapper.toRatingDto(rating);
        } else {
            if (likes) {
                event.setLikes(event.getLikes() != null ? (event.getLikes() + 1) : 1);
                initiator.setLikes(initiator.getLikes() != null ? (initiator.getLikes() + 1) : 1);
                event.setDislikes(event.getDislikes() - 1);
                initiator.setDislikes(initiator.getDislikes() - 1);
            } else {
                event.setLikes(event.getLikes() - 1);
                initiator.setLikes(initiator.getLikes() - 1);
                event.setDislikes(event.getDislikes() != null ? event.getDislikes() + 1 : 1);
                initiator.setDislikes(initiator.getDislikes() != null ? initiator.getDislikes() + 1 : 1);
            }
            rating.setLikes(likes);
            setRate(event, initiator);

            return RatingMapper.toRatingDto(ratingRepository.save(rating));
        }
    }

    private void deleteRating(User initiator, Event event, Rating rating) {
        if (rating.getLikes()) {
            event.setLikes(event.getLikes() != null ? event.getLikes() - 1 : 0);
            initiator.setLikes(initiator.getLikes() != null ? initiator.getLikes() - 1 : 0);
        } else {
            event.setDislikes(event.getDislikes() != null ? event.getDislikes() - 1 : 0);
            initiator.setDislikes(initiator.getDislikes() != null ? initiator.getDislikes() - 1 : 0);
        }
        setRate(event, initiator);
        ratingRepository.deleteById(rating.getId());
    }

    private void setRate(Event event, User initiator) {
        int eventLike = event.getLikes() != null ? event.getLikes() : 0;
        int eventDislike = event.getDislikes() != null ? event.getDislikes() : 0;

        int totalEventVotes = eventLike + eventDislike;
        double rateEvent = (eventLike * 10.0) / totalEventVotes;
        if (rateEvent <= 0) {
            event.setRate(null);
        } else {
            event.setRate(Math.round(rateEvent * 10) / 10.0);
        }

        int userLike = initiator.getLikes() != null ? initiator.getLikes() : 0;
        int userDislike = initiator.getDislikes() != null ? initiator.getDislikes() : 0;

        int totalUserVotes = userLike + userDislike;
        double rateUser = (userLike * 10.0) / totalUserVotes;
        if (rateUser <= 0) {
            initiator.setRate(null);
        } else {
            initiator.setRate(Math.round(rateUser * 10) / 10.0);
        }

        eventRepository.save(event);
        userRepository.save(initiator);
    }
}
