package ru.practicum.mainservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainsevice.enums.RequestStatus;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "requests", schema = "public")
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
