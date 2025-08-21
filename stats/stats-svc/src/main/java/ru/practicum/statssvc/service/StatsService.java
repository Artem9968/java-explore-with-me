package ru.practicum.statssvc.service;

import ru.practicum.statsdto.HitDto;
import ru.practicum.statsdto.StatsDto;

import java.util.List;

public interface StatsService {

    void registerHit(HitDto hitDto);

    List<StatsDto> getStatistics(String startText, String endText,
                                 List<String> uris, Boolean unique, Integer limit);
}