package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDtoRequest;
import ru.practicum.ewm.user.dto.UserDtoResponse;

import java.util.List;

public interface UserService {
    UserDtoResponse save(UserDtoRequest userDto);

    List<UserDtoResponse> findAllByIds(List<Long> ids, int from, int size);

    void delete(long id);
}
