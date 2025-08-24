package ru.practicum.mainservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainsevice.enums.EventState;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "events", schema = "public")
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "annotation", nullable = false)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "createdon", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "description")
    private String description;
    @Column(name = "eventdate", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participantlimit")
    private Integer participantLimit;
    @Column(name = "publishedon")
    private LocalDateTime publishedOn;
    @Column(name = "requestmoderation")
    private Boolean requestModeration;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Column(name = "title")
    private String title;
    @Transient
    private Integer confirmedRequests;
    @Transient
    private Integer views;
}
