package ru.practicum.statssvc.mapper;

import ru.practicum.statsdto.StatsDto;
import ru.practicum.statssvc.model.EndpointStats;

public class StatsResponseMapper {

    public static StatsDto toDto(EndpointStats viewStats) {
        StatsDto statsDto = new StatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
        return statsDto;
    }
}
