package ru.practicum.ewm.users.service;

import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.dto.UserRateDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size);

    UserDto addUser(UserDto userDto);

    void deleteUser(Long userId);

    List<UserRateDto> getRatedUsers(Long userId, String rateSort, Integer from, Integer size);
}