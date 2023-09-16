package ru.practicum.ewm.main.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private Long rating;
}
