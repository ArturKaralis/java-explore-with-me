package ru.practicum.ewm.requests.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.requests.enums.RequestStatus;
import ru.practicum.ewm.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_request")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created", nullable = false)
    LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
}
