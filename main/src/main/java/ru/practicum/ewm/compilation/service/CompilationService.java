package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDtoCreationRequest;
import ru.practicum.ewm.compilation.dto.CompilationDtoResponse;
import ru.practicum.ewm.compilation.dto.CompilationDtoUpdateRequest;

import java.util.List;

public interface CompilationService {
    CompilationDtoResponse save(CompilationDtoCreationRequest compilationDtoRequest);

    List<CompilationDtoResponse> findAll(Boolean pinned, int from, int size);

    CompilationDtoResponse findById(long compId);

    CompilationDtoResponse update(long compId, CompilationDtoUpdateRequest compilationDtoRequest);

    void delete(long compId);
}
