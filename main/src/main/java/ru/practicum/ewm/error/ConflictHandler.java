package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictHandler {

    @ExceptionHandler
    public ApiError handleConflictException(final DataIntegrityViolationException e) {
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Integrity constraint has been violated.")
                .status(HttpStatus.CONFLICT.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}