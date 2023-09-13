package ru.practicum.ewm.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {

    @NotBlank(message = "App can't be Blank.")
    String app;

    @NotBlank(message = "Uri can't be Blank.")
    String uri;

    Long hits;
}
