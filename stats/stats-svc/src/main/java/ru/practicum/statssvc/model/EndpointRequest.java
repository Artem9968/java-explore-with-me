package ru.practicum.statssvc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class EndpointRequest {

    private Integer id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;

}
