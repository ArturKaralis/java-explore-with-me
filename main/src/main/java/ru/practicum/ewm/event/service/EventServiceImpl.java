package ru.practicum.ewm.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dao.LocationRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.rating.dao.RatingRepository;
import ru.practicum.ewm.rating.dto.CountRatingDto;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.request.dao.ParticipationRequestRepository;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.statistics.client.client.StatisticsClient;
import ru.practicum.ewm.statistics.dto.ResponseHitDto;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.exception.DbConflictException;
import ru.practicum.ewm.exception.IncorrectIdException;
import ru.practicum.ewm.exception.IncorrectSortException;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static ru.practicum.ewm.event.dto.EventSort.EVENT_DATE;
import static ru.practicum.ewm.event.dto.EventSort.getSort;
import static ru.practicum.ewm.event.mapper.EventMapper.*;
import static ru.practicum.ewm.event.mapper.LocationMapper.*;
import static ru.practicum.ewm.request.mapper.ParticipationRequestMapper.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final Sort BY_EVENT_DATE_DESC = Sort.by(Sort.Direction.DESC, "eventDate");
    private static final String PENDING_REQUESTS = "pendingRequests";
    private static final String CONFIRMED_REQUESTS = "confirmedRequests";
    private static final String REJECTED_REQUESTS = "rejectedRequests";

    private final StatisticsClient statClient;
    private final ObjectMapper objectMapper;

    private final EventRepository repo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final LocationRepository locationRepo;
    private final ParticipationRequestRepository participationRequestRepo;
    private final RatingRepository ratingRepo;
    @Value("${ewm-app.name}")
    private String app;

    @Transactional
    @Override
    public EventDtoResponse save(EventDtoCreationRequest eventDtoCreationRequest, long userId) {
        return mapToEventDtoResponse(repo.saveAndFlush(mapToEvent(
                eventDtoCreationRequest,
                findCategory(eventDtoCreationRequest.getCategory()),
                findInitiator(userId),
                getLocation(eventDtoCreationRequest.getLocation()))));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventDtoResponse> findAll(String text,
                                          List<Long> categories,
                                          Boolean paid,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          boolean onlyAvailable,
                                          String sort,
                                          int from,
                                          int size,
                                          String ip) {
        final List<Event> events;
        final EventSort sortFromString = getSort(sort);
        if (text != null) {
            text = text.toLowerCase();
        }
        if (sortFromString.equals(EVENT_DATE)) {
            events = repo.findAll(
                    State.PUBLISHED,
                    text,
                    categories,
                    paid,
                    rangeStart,
                    rangeEnd,
                    PageRequest.of(from / size, size, BY_EVENT_DATE_DESC));
        } else {
            events = repo.findAll(
                    State.PUBLISHED,
                    text,
                    categories,
                    paid,
                    rangeStart,
                    rangeEnd,
                    PageRequest.of(from / size, size));
        }
        List<EventDtoResponse> eventDtoResponses;
        switch (sortFromString) {
            case VIEWS:
                eventDtoResponses = setParticipationRequestAndViews(rangeStart, rangeEnd, events)
                        .stream()
                        .sorted(comparing(EventDtoResponse::getViews))
                        .collect(toList());
                break;
            case LIKE:
                eventDtoResponses = setParticipationRequestAndViews(rangeStart, rangeEnd, events)
                        .stream()
                        .sorted(comparing(EventDtoResponse::getCountLike).reversed())
                        .collect(toList());
                break;
            case DISLIKE:
                eventDtoResponses = setParticipationRequestAndViews(rangeStart, rangeEnd, events)
                        .stream()
                        .sorted(comparing(EventDtoResponse::getCountDislike).reversed())
                        .collect(toList());
                break;
            case RATING:
                eventDtoResponses = setParticipationRequestAndViews(rangeStart, rangeEnd, events)
                        .stream()
                        .sorted(Comparator.comparing(e -> e.getCountLike() - e.getCountDislike()))
                        .collect(toList());
                break;
            case NOT_SORT:
            case EVENT_DATE:
                eventDtoResponses = setParticipationRequestAndViews(rangeStart, rangeEnd, events);
                break;
            default:
                throw new IncorrectSortException();
        }
        if (onlyAvailable) {
            eventDtoResponses = eventDtoResponses
                    .stream()
                    .filter(e -> e.getConfirmedRequests() != e.getParticipantLimit())
                    .collect(toList());
        }
        statClient.hit(app, "/events", ip);
        eventDtoResponses.forEach(e -> statClient.hit(app, String.format("/events/%d", e.getId()), ip));
        return eventDtoResponses;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventDtoResponse> findAll(List<Long> users,
                                          List<String> states,
                                          List<Long> categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          int from,
                                          int size) {
        final List<Event> events = repo.findAll(
                users,
                Optional.ofNullable(states).orElse(emptyList()).stream().map(State::valueOf).collect(toList()),
                categories,
                rangeStart,
                rangeEnd,
                PageRequest.of(from / size, size));
        return setParticipationRequestAndViews(rangeStart, rangeEnd, events)
                .stream()
                .sorted(Comparator.comparing(EventDtoResponse::getId))
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventDtoResponse> findAll(long userId, int from, int size) {
        final User initiator = findInitiator(userId);
        final List<Event> events = repo.findAllByInitiator(initiator, PageRequest.of(from / size, size));
        return setParticipationRequestAndRating(events);
    }

    @Transactional(readOnly = true)
    @Override
    public EventDtoResponse findById(long eventId, String ip) {
        final Event event = repo.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new IncorrectIdException(eventId, "event"));
        final List<ParticipationRequest> requests = participationRequestRepo
                .findAllByEventAndStatus(event, Status.CONFIRMED);
        final long countLike = ratingRepo.countByEventAndRating(event, Rating.LIKE);
        final long countDislike = ratingRepo.countByEventAndRating(event, Rating.DISLIKE);
        final EventDtoResponse eventDtoResponse = mapToEventDtoResponse(event, requests.size(), countLike, countDislike);
        try {
            List<ResponseHitDto> views = objectMapper.readValue(statClient.getStats(event.getPublishedOn(),
                    LocalDateTime.now(),
                    List.of(String.format("/events/%d", eventId)),
                    true).getBody().toString(), new TypeReference<>() {
            });
            if (views == null || views.isEmpty()) {
                eventDtoResponse.setViews(0);
            } else {
                eventDtoResponse.setViews(views.get(0).getHits());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        statClient.hit(app, String.format("/events/%d", eventId), ip);
        return eventDtoResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public EventDtoResponse findById(long userId, long eventId) {
        final Event event = findEventByInitiatorIdAndEventId(userId, eventId);
        final List<ParticipationRequest> requests = participationRequestRepo.findAllByEvent(event);
        final long countLike = ratingRepo.countByEventAndRating(event, Rating.LIKE);
        final long countDislike = ratingRepo.countByEventAndRating(event, Rating.DISLIKE);
        return mapToEventDtoResponse(event, requests.size(), countLike, countDislike);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findRequestByUserIdAndById(long userId, long eventId) {
        final Event event = findEventByInitiatorIdAndEventId(userId, eventId);
        return mapToParticipantRequestDto(participationRequestRepo.findAllByEvent(event));
    }

    @Transactional
    @Override
    public EventDtoResponse update(EventDtoUpdateRequest eventDtoRequest,
                                   long userId,
                                   long eventId) {
        final Event event = findEventByInitiatorIdAndEventId(userId, eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new DbConflictException();
        }
        return mapToEventDtoResponse(update(event, eventDtoRequest));
    }

    @Transactional
    @Override
    public EventDtoResponse update(EventDtoUpdateRequest eventDtoRequest, long eventId) {
        final Event event = repo.findById(eventId).orElseThrow(() -> new IncorrectIdException(eventId, "event"));
        if (!event.getState().equals(State.PENDING)) {
            throw new DbConflictException();
        }
        return mapToEventDtoResponse(update(event, eventDtoRequest));
    }

    @Transactional
    @Override
    public Map<String, List<ParticipationRequestDto>> updateRequests(EventRequestStatusUpdateRequest dto,
                                                                     long userId,
                                                                     long eventId) {
        final Event event = findEventByInitiatorIdAndEventId(userId, eventId);
        final List<ParticipationRequest> requests = participationRequestRepo.findAllByEvent(event);
        if (requests == null || requests.isEmpty() || dto.getRequestIds() == null || dto.getRequestIds().isEmpty()) {
            throw new DbConflictException();
        } else if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            requests.forEach(pr -> pr.setStatus(Status.CONFIRMED));
            return Map.of(CONFIRMED_REQUESTS, mapToParticipantRequestDto(requests));
        } else {
            Map<Status, List<ParticipationRequest>> byStatus = requests
                    .stream()
                    .collect(groupingBy(ParticipationRequest::getStatus, toList()));
            switch (dto.getStatus()) {
                case CONFIRMED:
                    int limit = event.getParticipantLimit();
                    int confirmedNow = byStatus.getOrDefault(Status.CONFIRMED, emptyList()).size();
                    int requiredConfirmed = dto.getRequestIds().size();
                    if (limit == confirmedNow && !byStatus.containsKey(Status.PENDING)) {
                        return Map.of(
                                CONFIRMED_REQUESTS, mapToParticipantRequestDto(byStatus.get(Status.CONFIRMED)),
                                REJECTED_REQUESTS, mapToParticipantRequestDto(byStatus.get(Status.REJECTED)));
                    } else {
                        List<ParticipationRequest> required = requests
                                .stream()
                                .filter(pr -> dto.getRequestIds().contains(pr.getId()) && pr.getStatus().equals(Status.PENDING))
                                .collect(toList());
                        if (confirmedNow + requiredConfirmed >= limit) {
                            required.subList(0, limit - confirmedNow)
                                    .forEach(pr -> pr.setStatus(Status.CONFIRMED));
                            required.subList(limit - confirmedNow, required.size() - 1)
                                    .forEach(pr -> pr.setStatus(Status.REJECTED));
                        } else {
                            required.forEach(ps -> ps.setStatus(Status.CONFIRMED));
                        }
                    }
                    break;
                case REJECTED:
                    requests.stream()
                            .filter(pr -> dto.getRequestIds().contains(pr.getId()) && pr.getStatus().equals(Status.PENDING))
                            .forEach(pr -> pr.setStatus(Status.REJECTED));
            }
            byStatus = requests
                    .stream()
                    .collect(groupingBy(ParticipationRequest::getStatus, toList()));
            return Map.of(
                    PENDING_REQUESTS,
                    mapToParticipantRequestDto(byStatus.getOrDefault(Status.PENDING, emptyList())),
                    CONFIRMED_REQUESTS,
                    mapToParticipantRequestDto(byStatus.getOrDefault(Status.CONFIRMED, emptyList())),
                    REJECTED_REQUESTS,
                    mapToParticipantRequestDto(byStatus.getOrDefault(Status.REJECTED, emptyList())));
        }
    }

    private Event update(Event event, EventDtoUpdateRequest eventDtoRequest) {
        if (eventDtoRequest.getAnnotation() != null && !eventDtoRequest.getAnnotation().isBlank()) {
            event.setAnnotation(eventDtoRequest.getAnnotation());
        }
        if (eventDtoRequest.getCategory() != null) {
            event.setCategory(findCategory(eventDtoRequest.getCategory()));
        }
        if (eventDtoRequest.getDescription() != null && !eventDtoRequest.getDescription().isBlank()) {
            event.setDescription(eventDtoRequest.getDescription());
        }
        if (eventDtoRequest.getEventDate() != null) {
            event.setEventDate(eventDtoRequest.getEventDate());
        }
        if (eventDtoRequest.getLocation() != null) {
            event.setLocation(getLocation(eventDtoRequest.getLocation()));
        }
        if (eventDtoRequest.getPaid() != null) {
            event.setPaid(eventDtoRequest.getPaid());
        }
        if (eventDtoRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDtoRequest.getParticipantLimit());
        }
        if (eventDtoRequest.getRequestModeration() != null) {
            event.setRequestModeration(eventDtoRequest.getRequestModeration());
        }
        if (eventDtoRequest.getStateAction() != null) {
            switch (eventDtoRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(State.REJECTED);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
            }
        }
        if (eventDtoRequest.getTitle() != null && !eventDtoRequest.getTitle().isBlank()) {
            event.setTitle(eventDtoRequest.getTitle());
        }
        return event;
    }

    private Location getLocation(LocationDto locationDto) {
        return locationRepo.saveAndFlush(mapToLocation(locationDto));
    }

    private Category findCategory(long categoryId) {
        return categoryRepo
                .findById(categoryId)
                .orElseThrow(() -> new IncorrectIdException(categoryId, "category"));
    }

    private Event findEventByInitiatorIdAndEventId(long initiatorId, long eventId) {
        final User initiator = findInitiator(initiatorId);
        return repo.findByIdAndInitiator(eventId, initiator);
    }

    private User findInitiator(long userId) {
        return userRepo
                .findById(userId)
                .orElseThrow(() -> new IncorrectIdException(userId, "user"));
    }

    private List<EventDtoResponse> setParticipationRequestAndViews(LocalDateTime rangeStart,
                                                                   LocalDateTime rangeEnd,
                                                                   List<Event> events) {
        final List<String> uris = events.stream().map(e -> String.format("/events/%d", e.getId())).collect(toList());
        final List<EventDtoResponse> eventDtoResponses = setParticipationRequestAndRating(events);
        if (rangeStart == null) {
            Optional<Event> firstPublished = events
                    .stream()
                    .filter(e -> e.getState().equals(State.PUBLISHED)).min(comparing(Event::getPublishedOn));
            if (firstPublished.isPresent()) {
                rangeStart = firstPublished.get().getPublishedOn();
            } else {
                eventDtoResponses.forEach(e -> e.setViews(0L));
                return eventDtoResponses;
            }
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now();
        }
        try {
            List<ResponseHitDto> views = objectMapper.readValue(
                    Objects.requireNonNull(statClient.getStats(rangeStart, rangeEnd, uris, true).getBody()).toString(), new TypeReference<>() {
                    });
            Map<Long, Integer> viewsById = new HashMap<>();
            for (ResponseHitDto v : views) {
                if (viewsById.put(Long.parseLong(v.getUri().substring("/events/".length())), Math.toIntExact(v.getHits())) != null) {
                    throw new IllegalStateException("Duplicate key");
                }
            }
            eventDtoResponses.forEach(e -> e.setViews(viewsById.getOrDefault(e.getId(), 0)));
            return eventDtoResponses;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<EventDtoResponse> setParticipationRequestAndRating(List<Event> events) {
        final List<CountRequestDto> countRequests = participationRequestRepo.findAllCountRequestByEvents(events);
        final List<CountRatingDto> countLike = ratingRepo.findAllCountRatingByEventsAndRating(events, Rating.LIKE);
        final List<CountRatingDto> countDislike = ratingRepo.findAllCountRatingByEventsAndRating(events, Rating.DISLIKE);
        return mapToEventDtoResponse(events, countRequests, countLike, countDislike);
    }
}
