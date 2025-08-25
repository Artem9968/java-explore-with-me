package ru.practicum.mainservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.enums.EventStatus;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Column(nullable = false, name = "annotation")
    private String annotation;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EventStatus state;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

    @Column(nullable = false, name = "createdon")
    private LocalDateTime creationTimestamp;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "participantlimit")
    private Integer maxAttendees;

    @Column(name = "publishedon")
    private LocalDateTime publicationTime;

    @Transient
    private Integer viewCount;

    @Column(name = "requestmoderation")
    private Boolean requiresApproval;

    @Column(nullable = false, name = "eventdate")
    private LocalDateTime scheduledTime;

    @JoinColumn(name = "initiator_id")
    @ManyToOne
    private User organizer;

    @Column(name = "lat")
    private Float latitude;

    @Column(name = "lon")
    private Float longitude;

    @Column(name = "paid")
    private Boolean isPaid;

    @Transient
    private Integer approvedParticipants;
}
