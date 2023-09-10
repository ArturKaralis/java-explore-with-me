package ru.practicum.ewm.event.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(length = 2000)
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @Column
    LocalDateTime createdOn;
    @Column(length = 7000)
    String description;
    @Column
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @OneToOne
    @JoinColumn(name = "location_id")
    Location location;
    @Column
    boolean paid;
    @Column
    int participantLimit;
    @Column
    LocalDateTime publishedOn;
    @Column
    boolean requestModeration;
    @Enumerated(EnumType.STRING)
    State state;
    @Column(length = 120)
    String title;
}
