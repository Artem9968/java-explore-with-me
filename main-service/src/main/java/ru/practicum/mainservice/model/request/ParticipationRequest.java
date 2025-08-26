package ru.practicum.mainservice.model.request;

import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import java.time.LocalDateTime;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.model.enums.RequestStatus;
import ru.practicum.mainservice.model.event.Event;

@Entity
@Table(name = "requests", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
