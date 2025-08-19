package ru.practicum.statdto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatsDto {
    private String app;
    private String uri;
    private Integer hits;
}
