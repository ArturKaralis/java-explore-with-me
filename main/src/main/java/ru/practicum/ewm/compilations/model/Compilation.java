package ru.practicum.ewm.compilations.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.events.model.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "pinned")
    Boolean pinned;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "compilation_events", joinColumns = @JoinColumn(name = "compilation_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
    Set<Event> events;
}
