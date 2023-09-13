package ru.practicum.ewm.compilations.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationControllerPublic {

    CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pined,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получаем запрос на список подборки: pined={}, from={}, size={}", pined, from, size);
        List<CompilationDto> compilationDtoList = compilationService.getCompilations(pined, from, size);
        log.info("Возвращаем {} элемент(а/ов)", compilationDtoList.size());
        return compilationDtoList;
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilationById(@PathVariable Long compilationId) {
        log.info("Получаем запрос на конкретную подборку: compilationId={}", compilationId);
        CompilationDto compilationDto = compilationService.getCompilationById(compilationId);
        log.info("Возвращаем compilationDto={}", compilationDto);
        return compilationDto;
    }
}
