package ru.practicum.ewm.compilations.service;

import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.model.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest updateCompilationRequest);

    void deleteCompilation(Long compilationId);

    CompilationDto getCompilationById(Long compilationId);

    List<CompilationDto> getCompilations(Boolean pined, Integer from, Integer size);


}
