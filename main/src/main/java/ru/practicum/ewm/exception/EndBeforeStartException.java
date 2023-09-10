package ru.practicum.ewm.exception;

import java.time.LocalDateTime;

public class EndBeforeStartException extends RuntimeException {
    private final String message;

    public EndBeforeStartException() {
        this.message = "Ошибка! Команда завершилась до начала";
    }

    public EndBeforeStartException(LocalDateTime start, LocalDateTime end) {
        this.message = String.format("Время завершения (%s) до начала (%s)", end.toString(), start.toString());
    }

    @Override
    public String getMessage() {
        return message;
    }
}
