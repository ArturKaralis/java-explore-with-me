package ru.practicum.ewm.exception;

public class DbConflictException extends RuntimeException {
    private final String message;

    public DbConflictException() {
        this.message = "Ошибка базы данных!";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
