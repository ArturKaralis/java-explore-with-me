package ru.practicum.ewm.exception;

public class IncorrectSortException extends RuntimeException {
    private final String message;

    public IncorrectSortException() {
        message = "Сортировка не определена: UNSUPPORTED_SORT";
    }

    public IncorrectSortException(String sort) {
        message = String.format("Сортировка не определена: %s", sort);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
