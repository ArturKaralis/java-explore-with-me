package ru.practicum.ewm.exception;

public class CustomDateSerializerException extends RuntimeException {
    private final String message;

    public CustomDateSerializerException() {
        this.message = "Возникло исключение при серриализации!";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
