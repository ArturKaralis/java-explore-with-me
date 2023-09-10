package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.exception.IncorrectSortException;

public enum EventSort {
    EVENT_DATE, VIEWS, NOT_SORT, LIKE, DISLIKE, RATING;

    public static EventSort getSort(String sort) {
        if (sort == null) {
            return NOT_SORT;
        }
        try {
            return EventSort.valueOf(sort);
        } catch (Throwable e) {
            throw new IncorrectSortException(sort);
        }
    }
}