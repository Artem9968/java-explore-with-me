package ru.practicum.statssvc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class EndpointStats {

    private String app;

    private String uri;

    private Integer hits;

}
