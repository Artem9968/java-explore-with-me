package ru.practicum.statsvc.repository;

import ru.practicum.statsvc.model.EndpointHit;
import ru.practicum.statsvc.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatStorage {
    EndpointHit addHit(EndpointHit hit);

    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique, Integer size);
}
