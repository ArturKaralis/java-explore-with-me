package ru.practicum.ewm.users.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserControllerAdmin {

    UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                  @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получаем запрос: userIds={}, from={}, size={}", ids, from, size);
        List<UserDto> users = userService.getUsers(ids, from, size);
        log.info("Возвращаем {} элемент(а/ов).", users.size());
        return users;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        log.info("Получаем запрос: userDto={}", userDto);
        UserDto newUserDto = userService.addUser(userDto);
        log.info("Возвращаем ответ userDto={}", newUserDto);
        return newUserDto;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получаем запрос на удаление: userId={}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь userId={} удален.", userId);
    }
}