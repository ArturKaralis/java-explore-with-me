package ru.practicum.ewm.compilations.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.mapper.CompilationMapper;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.compilations.model.UpdateCompilationRequest;
import ru.practicum.ewm.compilations.repository.CompilationRepository;
import ru.practicum.ewm.events.repository.EventRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationServiceImpl implements CompilationService {

    CompilationRepository compilationRepository;

    EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        newCompilationDto.setPinned(newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false);
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(newCompilationDto.getEvents().stream()
                    .flatMap(ids -> eventRepository.findAllById(Collections.singleton(ids)).stream())
                    .collect(Collectors.toSet()));
        } else {
            compilation.setEvents(new HashSet<>());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found."));
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(updateCompilationRequest.getEvents().stream()
                    .flatMap(ids -> eventRepository.findAllById(Collections.singleton(ids)).stream())
                    .collect(Collectors.toSet()));
        }
        compilation.setPinned(updateCompilationRequest.getPined() != null ? updateCompilationRequest.getPined() : compilation.getPinned());
        compilation.setTitle(updateCompilationRequest.getTitle() != null ? updateCompilationRequest.getTitle() : compilation.getTitle());
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        compilationRepository.findById(compilationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found."));
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compilationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found.")));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pined, Integer from, Integer size) {
        int pageNumber = (int) Math.ceil((double) from / size);
        Pageable pageable = PageRequest.of(pageNumber, size);
        if (pined != null) {
            return compilationRepository.findAllByPinned(pined, pageable).map(CompilationMapper::toCompilationDto).toList();
        } else {
            return compilationRepository.findAll(pageable).map(CompilationMapper::toCompilationDto).toList();
        }
    }
}
