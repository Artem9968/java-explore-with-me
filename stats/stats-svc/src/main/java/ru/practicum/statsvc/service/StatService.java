package ru.practicum.statsvc.service;

import ru.practicum.statdto.HitDto;
import ru.practicum.statdto.StatsDto;

import java.util.List;

public interface StatService {

    public void addHit(HitDto hitDto);

    public List<StatsDto> getStats(String startTxt, String endTxt, List<String> uris, Boolean unique, Integer size);
}
