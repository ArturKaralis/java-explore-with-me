package ru.practicum.ewm.compilations.mapper;

import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.events.mapper.EventMapper;

import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()),
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }
}
