package ru.practicum.ewm.users.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortDto {
    @NotNull(message = "UserId can't be null.")
    Long id;

    @NotNull(message = "User name can't be Null")
    @NotBlank(message = "User name can't be Blank")
    String name;
}