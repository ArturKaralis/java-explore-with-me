package ru.practicum.ewm.main.event.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Location {

    public float lon;
    private float lat;

}
