package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestHandler {

    @ExceptionHandler
    public ApiError handleBadRequestException(final MethodArgumentTypeMismatchException e) {
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Incorrectly request.")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}