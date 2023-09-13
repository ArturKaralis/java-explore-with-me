package ru.practicum.ewm.users.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "email")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;

    @Size(min = 2, max = 250)
    @NotBlank(message = "User name can't be Blank")
    String name;

    @Size(min = 6, max = 254)
    @NotBlank(message = "User email can't be Blank")
    @Email(message = "Invalid email format")
    String email;
}