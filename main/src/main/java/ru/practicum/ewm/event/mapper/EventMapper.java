package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.CountRequestDto;
import ru.practicum.ewm.event.dto.EventDtoCreationRequest;
import ru.practicum.ewm.event.dto.EventDtoResponse;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.rating.dto.CountRatingDto;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static ru.practicum.ewm.event.mapper.LocationMapper.*;

@UtilityClass
public class EventMapper {
    public List<EventDtoResponse> mapToEventDtoResponse(List<Event> events,
                                                        List<CountRequestDto> countRequests,
                                                        List<CountRatingDto> countLike,
                                                        List<CountRatingDto> countDislike) {
        final Map<Long, Long> countRequestByEventId = countRequests
                .stream()
                .collect(toMap(CountRequestDto::getEventId, CountRequestDto::getCountRequest));
        final Map<Long, Long> countLikeByEventId = countLike
                .stream()
                .collect(toMap(CountRatingDto::getEventId, CountRatingDto::getCountRating));
        final Map<Long, Long> countDislikeByEventId = countDislike
                .stream()
                .collect(toMap(CountRatingDto::getEventId, CountRatingDto::getCountRating));
        return events
                .stream()
                .map(e -> mapToEventDtoResponse(e,
                        countRequestByEventId.getOrDefault(e.getId(), 0L),
                        countLikeByEventId.getOrDefault(e.getId(), 0L),
                        countDislikeByEventId.getOrDefault(e.getId(), 0L)))
                .collect(Collectors.toList());
    }

    public List<EventDtoResponse> mapToEventDtoResponse(List<Event> events, List<CountRequestDto> countRequests) {
        final Map<Long, Long> countRequestByEventId = countRequests
                .stream()
                .collect(toMap(CountRequestDto::getEventId, CountRequestDto::getCountRequest));
        return events
                .stream()
                .map(e -> mapToEventDtoResponse(e, countRequestByEventId.getOrDefault(e.getId(), 0L)))
                .collect(Collectors.toList());
    }

    public EventDtoResponse mapToEventDtoResponse(Event event,
                                                  long confirmedRequests,
                                                  long countLike,
                                                  long countDislike) {
        EventDtoResponse eventDtoResponse = mapToEventDtoResponse(event, confirmedRequests);
        eventDtoResponse.setCountLike(countLike);
        eventDtoResponse.setCountDislike(countDislike);
        return eventDtoResponse;
    }

    public EventDtoResponse mapToEventDtoResponse(Event event, long confirmedRequests) {
        EventDtoResponse eventDtoResponse = mapToEventDtoResponse(event);
        eventDtoResponse.setConfirmedRequests(confirmedRequests);
        return eventDtoResponse;
    }

    public EventDtoResponse mapToEventDtoResponse(Event event) {
        EventDtoResponse eventDtoResponse = EventDtoResponse
                .builder()
                .annotation(event.getAnnotation())
                .category(new EventDtoResponse.Category(event.getCategory().getId(), event.getCategory().getName()))
                .confirmedRequests(0)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(new EventDtoResponse.Initiator(event.getInitiator().getId(), event.getInitiator().getName()))
                .location(mapToLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
        if (event.getPublishedOn() != null) {
            eventDtoResponse.setPublishedOn(event.getPublishedOn());
        }
        return eventDtoResponse;
    }

    public Event mapToEvent(EventDtoCreationRequest eventDtoCreationRequest,
                            Category category,
                            User initiator,
                            Location location) {
        Event event = new Event();
        event.setAnnotation(eventDtoCreationRequest.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(eventDtoCreationRequest.getDescription());
        event.setEventDate(eventDtoCreationRequest.getEventDate());
        event.setInitiator(initiator);
        event.setLocation(location);
        event.setPaid(eventDtoCreationRequest.isPaid());
        event.setParticipantLimit(eventDtoCreationRequest.getParticipantLimit());
        if (eventDtoCreationRequest.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else {
            event.setRequestModeration(eventDtoCreationRequest.getRequestModeration());
        }
        event.setState(State.PENDING);
        event.setTitle(eventDtoCreationRequest.getTitle());
        return event;
    }
}
