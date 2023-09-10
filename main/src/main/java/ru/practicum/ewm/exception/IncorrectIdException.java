package ru.practicum.ewm.exception;

public class IncorrectIdException extends RuntimeException {
    private final String message;

    public IncorrectIdException() {
        message = "Неопознанный ID: INCORRECT_ID";
    }

    public IncorrectIdException(long id) {
        message = String.format("Неопознанный ID: %d", id);
    }

    public IncorrectIdException(long id, String name) {
        message = String.format("Неизвестный %s_id: %d", name, id);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
