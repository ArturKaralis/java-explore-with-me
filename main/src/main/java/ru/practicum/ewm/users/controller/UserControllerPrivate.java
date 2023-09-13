package ru.practicum.ewm.users.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.users.dto.UserRateDto;
import ru.practicum.ewm.users.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/rating")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserControllerPrivate {

    UserService userService;

    @GetMapping
    public List<UserRateDto> getRatedUsers(@PathVariable Long userId,
                                           @RequestParam(defaultValue = "HIGH") String rateSort,
                                           @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получаем запрос от {} на получение списка пользователей с {} рейтингом.", userId, rateSort);
        List<UserRateDto> userRateDtoList = userService.getRatedUsers(userId, rateSort, from, size);
        log.info("Возвращаем {} элемент(а/ов).", userRateDtoList.size());
        return userRateDtoList;
    }
}
