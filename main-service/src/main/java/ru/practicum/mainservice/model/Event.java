package ru.practicum.mainservice.model;

import lombok.Getter;
import java.time.LocalDateTime;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import ru.practicum.mainservice.model.enums.EventState;
import jakarta.persistence.Transient;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.Setter;

@Entity
@Table(name = "events", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "eventdate", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "createdon", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "publishedon")
    private LocalDateTime publishedOn;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participantlimit")
    private Integer participantLimit;

    @Column(name = "requestmoderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EventState state;

    @Transient
    private Integer confirmedRequests;

    @Transient
    private Integer views;
}