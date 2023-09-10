package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dao.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationDtoCreationRequest;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;
import ru.practicum.ewm.compilation.dto.CompilationDtoUpdateRequest;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.CountRequestDto;
import ru.practicum.ewm.event.dto.EventDtoResponse;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dao.ParticipationRequestRepository;
import ru.practicum.ewm.exception.IncorrectIdException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static ru.practicum.ewm.compilation.mapper.CompilationMapper.*;
import static ru.practicum.ewm.event.mapper.EventMapper.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepo;
    private final EventRepository eventRepo;
    private final ParticipationRequestRepository participationRequestRepo;

    @Transactional
    @Override
    public CompilationDtoResponse save(CompilationDtoCreationRequest compilationDtoRequest) {
        final List<Event> events;
        if (compilationDtoRequest.getEvents() != null && !compilationDtoRequest.getEvents().isEmpty()) {
            events = eventRepo.findAllById(compilationDtoRequest.getEvents());
        } else {
            events = Collections.emptyList();
        }
        final Compilation compilation = compilationRepo.saveAndFlush(mapToCompilation(compilationDtoRequest, events));
        return mapToCompilationDtoResponse(compilation, setParticipationRequest(events));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDtoResponse> findAll(Boolean pinned, int from, int size) {
        final List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepo.findAll(PageRequest.of(from / size, size)).getContent();
        } else {
            compilations = compilationRepo.findAllByPinned(pinned, PageRequest.of(from / size, size));
        }
        final Map<Long, List<EventDtoResponse>> eventsById = compilations
                .stream()
                .collect(toMap(Compilation::getId, c -> setParticipationRequest(c.getEvents())));
        return mapToCompilationDtoResponse(compilations, eventsById);
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDtoResponse findById(long compId) {
        final Compilation compilation = compilationRepo
                .findById(compId)
                .orElseThrow(() -> new IncorrectIdException(compId, "compilation"));
        return mapToCompilationDtoResponse(compilation, setParticipationRequest(compilation.getEvents()));
    }

    @Transactional
    @Override
    public CompilationDtoResponse update(long compId, CompilationDtoUpdateRequest compilationDtoRequest) {
        final Compilation compilation = compilationRepo
                .findById(compId)
                .orElseThrow(() -> new IncorrectIdException(compId, "compilation"));
        List<Event> events = compilation.getEvents();
        if (compilationDtoRequest.getEvents() != null && !compilationDtoRequest.getEvents().isEmpty()) {
            events = eventRepo.findAllById(compilationDtoRequest.getEvents());
            compilation.setEvents(events);
        }
        if (compilationDtoRequest.getPinned() != null) {
            compilation.setPinned(compilationDtoRequest.getPinned());
        }
        if (compilationDtoRequest.getTitle() != null && !compilationDtoRequest.getTitle().isBlank()) {
            compilation.setTitle(compilationDtoRequest.getTitle());
        }
        return mapToCompilationDtoResponse(compilation, setParticipationRequest(events));
    }

    @Transactional
    @Override
    public void delete(long compId) {
        compilationRepo.deleteById(compId);
    }

    private List<EventDtoResponse> setParticipationRequest(List<Event> events) {
        final List<CountRequestDto> countRequests = participationRequestRepo.findAllCountRequestByEvents(events);
        return mapToEventDtoResponse(events, countRequests);
    }
}
