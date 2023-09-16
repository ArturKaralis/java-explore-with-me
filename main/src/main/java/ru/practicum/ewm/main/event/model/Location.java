package ru.practicum.ewm.main.event.model;

import lombok.*;
import ru.practicum.ewm.main.validator.OnCreateValidation;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Location {

    @NotNull(groups = OnCreateValidation.class)
    public float lon;
    @NotNull(groups = OnCreateValidation.class)
    private float lat;

}
