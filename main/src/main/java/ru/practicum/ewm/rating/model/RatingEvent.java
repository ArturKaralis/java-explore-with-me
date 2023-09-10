package ru.practicum.ewm.rating.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table
public class RatingEvent {
    @EmbeddedId
    RatingEventKey id;
    @Enumerated(EnumType.STRING)
    Rating rating;
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @MapsId("event_id")
    @JoinColumn(name = "event_id")
    Event event;
}
