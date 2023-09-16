package ru.practicum.ewm.main.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmptyObjectException extends NullPointerException {
    @Getter
    private final String description;
}
