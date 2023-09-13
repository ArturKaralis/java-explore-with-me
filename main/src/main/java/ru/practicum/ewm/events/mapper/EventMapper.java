package ru.practicum.ewm.events.mapper;

import ru.practicum.ewm.categories.mapper.CategoryMapper;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventRatedDto;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.dto.NewEventDto;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.locations.mapper.LocationMapper;
import ru.practicum.ewm.users.mapper.UserMapper;

public class EventMapper {
    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .id(newEventDto.getId())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(LocationMapper.toLocation(newEventDto.getLocation()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventRatedDto toEventRatedDto(Event event) {
        return EventRatedDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .title(event.getTitle())
                .likes(event.getLikes())
                .dislikes(event.getDislikes())
                .rate(event.getRate())
                .views(event.getViews())
                .build();
    }
}
