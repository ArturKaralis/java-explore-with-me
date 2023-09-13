package ru.practicum.ewm.compilations.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.model.UpdateCompilationRequest;
import ru.practicum.ewm.compilations.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationControllerAdmin {
    CompilationService compilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получаем запрос на добавление подборки: newCompilationDto={}", newCompilationDto);
        CompilationDto compilationDto = compilationService.addCompilation(newCompilationDto);
        log.info("Возвращаем compilationDto={}", compilationDto);
        return compilationDto;
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilation(@PathVariable Long compilationId,
                                            @RequestBody @Valid UpdateCompilationRequest compilationRequest) {
        log.info("Получаем запрос на обновление подборки: updateCompilationRequest={}", compilationRequest);
        CompilationDto compilationDto = compilationService.updateCompilation(compilationId, compilationRequest);
        log.info("Возвращаем обновленную подборку: compilationDto={}", compilationDto);
        return compilationDto;
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compilationId) {
        log.info("Получаем запрос на удаление подборки: compilationId={}", compilationId);
        compilationService.deleteCompilation(compilationId);
        log.info("Подборка compilationId={} удалена.", compilationId);
    }
}
