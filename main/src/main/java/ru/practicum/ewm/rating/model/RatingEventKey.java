package ru.practicum.ewm.rating.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class RatingEventKey implements Serializable {
    @Column(name = "user_id")
    Long userId;
    @Column(name = "event_id")
    Long eventId;
}
