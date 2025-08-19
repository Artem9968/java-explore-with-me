package ru.practicum.statssvc.storage;

import ru.practicum.statssvc.model.EndpointRequest;
import ru.practicum.statssvc.model.EndpointStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsStorage {

    void saveRequest(EndpointRequest request);

    List<EndpointStats> getStatistics(LocalDateTime start, LocalDateTime end,
                                      List<String> uris, Boolean unique, Integer limit);
}