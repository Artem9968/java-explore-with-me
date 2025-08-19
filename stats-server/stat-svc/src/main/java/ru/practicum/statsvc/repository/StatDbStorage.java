package ru.practicum.statsvc.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.statsvc.exception.InternalServerException;
import ru.practicum.statsvc.mapper.ViewStatsRowMapper;
import ru.practicum.statsvc.model.EndpointHit;
import ru.practicum.statsvc.model.ViewStats;

import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class StatDbStorage implements StatStorage {
    private static final DateTimeFormatter DATA_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String SQL_INSERT_HIT = """
            INSERT INTO endpointhits (app, uri, ip, timestamp)
            VALUES ( :app, :uri, :ip, :timestamp)
            """;

    private final NamedParameterJdbcTemplate jdbc;

    public StatDbStorage(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public EndpointHit addHit(EndpointHit hit) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        try {
            jdbc.update(SQL_INSERT_HIT,
                    new MapSqlParameterSource()
                            .addValue("app", hit.getApp())
                            .addValue("uri", hit.getUri())
                            .addValue("ip", hit.getIp())
                            .addValue("timestamp", hit.getTimestamp().format(DATA_TIME_FORMATTER), Types.TIMESTAMP),
                    generatedKeyHolder, new String[]{"id"}
            );
        } catch (DataAccessException e) {
            throw new InternalServerException("Ошибка при сохранении в базу данных. " +
                    e.getMessage());
        }
        // получаем идентификатор
        final Integer hitId = generatedKeyHolder.getKey().intValue();
        hit.setId(hitId);

        return hit;
    }

    @Override
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique, Integer size) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT app, uri, count(ip) as hits FROM ");

        if (unique) {
            sql.append("(SELECT DISTINCT ON (ip) app, uri, ip, timestamp FROM endpointhits) AS unique_hits");

        } else {
            sql.append("endpointhits");
        }

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Boolean whereFlag = false;

        if (uris != null && !uris.isEmpty()) {
            sql.append(" WHERE uri IN (:uris)");
            parameters.addValue("uris", uris);
            whereFlag = true;
        }

        if (start != null) {
            if (whereFlag) {
                sql.append(" AND timestamp >= :start");
            } else {
                sql.append(" WHERE timestamp >= :start");
                whereFlag = true;
            }
            parameters.addValue("start", start);
        }

        if (end != null) {
            if (whereFlag) {
                sql.append(" AND timestamp < :end");
            } else {
                sql.append(" WHERE timestamp < :end");
            }
            parameters.addValue("end", end);
        }

        sql.append(" GROUP BY uri, app ORDER BY hits DESC");

        if (size != null) {
            parameters.addValue("size", size);
            sql.append(" LIMIT :size");
        }

        try {
            List<ViewStats> viewStatsList = jdbc.query(sql.toString(),
                    parameters,
                    new ViewStatsRowMapper());
            return viewStatsList;
        } catch (EmptyResultDataAccessException ignored) {
            return List.of();
        }
    }
}
