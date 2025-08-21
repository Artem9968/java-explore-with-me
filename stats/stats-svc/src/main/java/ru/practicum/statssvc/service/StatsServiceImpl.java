package ru.practicum.statssvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statsdto.HitDto;
import ru.practicum.statsdto.StatsDto;
import ru.practicum.statssvc.exception.ValidationException;
import ru.practicum.statssvc.mapper.HitRequestMapper;
import ru.practicum.statssvc.mapper.StatsResponseMapper;
import ru.practicum.statssvc.storage.StatsStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsStorage storage;

    @Override
    public void registerHit(HitDto hitDto) {
        storage.saveRequest(HitRequestMapper.fromDto(hitDto));
    }

    @Override
    public List<StatsDto> getStatistics(String startText,
                                        String endText,
                                        List<String> uris,
                                        Boolean unique,
                                        Integer limit) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        try {
            if (startText != null && !startText.isBlank()) {
                start = LocalDateTime.parse(startText, DATE_TIME_FORMATTER);
            }
            if (endText != null && !endText.isBlank()) {
                end = LocalDateTime.parse(endText, DATE_TIME_FORMATTER);
            }
        } catch (DateTimeParseException e) {
            throw new ValidationException("Неверный формат даты и времени. Используйте: ГГГГ-ММ-ДД ЧЧ:мм:сс");
        }

        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("Время начала периода должно предшествовать времени окончания");
        }

        return storage.getStatistics(start, end, uris, unique, limit)
                .stream()
                .map(StatsResponseMapper::toDto)
                .toList();
    }
}