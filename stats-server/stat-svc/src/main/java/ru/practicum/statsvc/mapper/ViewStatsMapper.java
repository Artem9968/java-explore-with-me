package ru.practicum.statsvc.mapper;

import ru.practicum.statdto.StatsDto;
import ru.practicum.statsvc.model.ViewStats;

public class ViewStatsMapper {
    private ViewStatsMapper() {
    }

    public static StatsDto toDto(ViewStats viewStats) {
        StatsDto statsDto = new StatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
        return statsDto;
    }
}
