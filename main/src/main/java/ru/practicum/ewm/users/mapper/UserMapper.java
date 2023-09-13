package ru.practicum.ewm.users.mapper;

import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.dto.UserRateDto;
import ru.practicum.ewm.users.dto.UserShortDto;
import ru.practicum.ewm.users.model.User;

public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static UserRateDto toUserRateDto(User user) {
        return UserRateDto.builder()
                .id(user.getId())
                .name(user.getName())
                .rate(user.getRate())
                .build();
    }
}