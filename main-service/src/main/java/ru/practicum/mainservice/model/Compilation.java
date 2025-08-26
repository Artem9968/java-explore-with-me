package ru.practicum.mainservice.model;

import lombok.Setter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import java.util.Set;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Column;
import java.util.HashSet;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;

@Entity
@Table(name = "compilations", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "pinned", nullable = false)
    private Boolean pinned;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = "eventlinks",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private Set<Event> events = new HashSet<>();
}
