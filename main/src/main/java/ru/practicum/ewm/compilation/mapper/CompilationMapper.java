package ru.practicum.ewm.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.dto.CompilationDtoCreationRequest;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventDtoResponse;
import ru.practicum.ewm.event.model.Event;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public List<CompilationDtoResponse> mapToCompilationDtoResponse(List<Compilation> compilations,
                                                                    Map<Long, List<EventDtoResponse>> eventsById) {
        return compilations
                .stream()
                .map(c -> mapToCompilationDtoResponse(c, eventsById.getOrDefault(c.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    public CompilationDtoResponse mapToCompilationDtoResponse(Compilation compilation,
                                                              List<EventDtoResponse> events) {
        return CompilationDtoResponse
                .builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation mapToCompilation(CompilationDtoCreationRequest compilationDtoRequest, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationDtoRequest.isPinned());
        compilation.setTitle(compilationDtoRequest.getTitle());
        compilation.setEvents(events);
        return compilation;
    }
}
