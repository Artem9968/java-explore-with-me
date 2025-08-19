package ru.practicum.statsvc.service;

import org.springframework.stereotype.Service;
import ru.practicum.statdto.HitDto;
import ru.practicum.statdto.StatsDto;
import ru.practicum.statsvc.exception.ValidationException;
import ru.practicum.statsvc.mapper.EndpointMapper;
import ru.practicum.statsvc.mapper.ViewStatsMapper;
import ru.practicum.statsvc.repository.StatDbStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class StatServiceImpl implements StatService {
    private static final DateTimeFormatter DATA_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatDbStorage storage;

    public StatServiceImpl(StatDbStorage storage) {
        this.storage = storage;
    }

    @Override
    public void addHit(HitDto hitDto) {
        storage.addHit(EndpointMapper.toEndpointHit(hitDto));
    }

    @Override
    public List<StatsDto> getStats(String startTxt,
                                   String endTxt,
                                   List<String> uris,
                                   Boolean unique,
                                   Integer size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        try {
            if (startTxt != null && !startTxt.isEmpty()) {
                start = LocalDateTime.parse(startTxt, DATA_TIME_FORMATTER);
            }
            if (endTxt != null && !endTxt.isEmpty()) {
                end = LocalDateTime.parse(endTxt, DATA_TIME_FORMATTER);
            }
        } catch (DateTimeParseException e) {
            throw new ValidationException("Некорректный формат времени. " + e.getMessage());
        }

        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new ValidationException("Указан недопустимый промежуток времени");
            }
        }
        return storage.getViewStats(start, end, uris, unique, size)
                .stream()
                .map(ViewStatsMapper::toDto)
                .toList();
    }
}
