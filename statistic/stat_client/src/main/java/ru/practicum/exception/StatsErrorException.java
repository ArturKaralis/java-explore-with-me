package ru.practicum.exception;

public class StatsErrorException extends RuntimeException {
    public StatsErrorException(String message) {
        super(message);
    }
}
