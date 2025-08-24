package ru.practicum.statssvc.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.statssvc.exception.InternalServerException;
import ru.practicum.statssvc.mapper.ViewStatsResultSetMapper;
import ru.practicum.statssvc.model.EndpointRequest;
import ru.practicum.statssvc.model.EndpointStats;

import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class StatsDbStorage implements StatsStorage {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String INSERT_HIT_SQL = """
            INSERT INTO endpoint_requests (app, uri, ip, timestamp)
            VALUES (:app, :uri, :ip, :timestamp)
            """;

    private final NamedParameterJdbcTemplate jdbc;

    public StatsDbStorage(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void saveRequest(EndpointRequest hit) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        try {
            jdbc.update(INSERT_HIT_SQL,
                    new MapSqlParameterSource()
                            .addValue("app", hit.getApp())
                            .addValue("uri", hit.getUri())
                            .addValue("ip", hit.getIp())
                            .addValue("timestamp", hit.getTimestamp().format(DATE_TIME_FORMATTER), Types.TIMESTAMP),
                    generatedKeyHolder, new String[]{"id"});
        } catch (DataAccessException e) {
            throw new InternalServerException("Database save error: " + e.getMessage());
        }

        hit.setId(generatedKeyHolder.getKey().intValue());
    }

    @Override
    public List<EndpointStats> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique, Integer limit) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        StringBuilder sql = new StringBuilder();

        if (Boolean.TRUE.equals(unique)) {
            sql.append("SELECT app, uri, COUNT(*) AS hits FROM (")
                    .append("  SELECT DISTINCT ON (app, uri, ip) app, uri, ip ")
                    .append("  FROM endpoint_requests ");

            boolean hasWhere = false;
            if (uris != null && !uris.isEmpty()) {
                sql.append("WHERE uri IN (:uris) ");
                parameters.addValue("uris", uris);
                hasWhere = true;
            }

            if (start != null) {
                sql.append(hasWhere ? "AND " : "WHERE ").append("timestamp >= :start ");
                parameters.addValue("start", start);
                hasWhere = true;
            }

            if (end != null) {
                sql.append(hasWhere ? "AND " : "WHERE ").append("timestamp < :end ");
                parameters.addValue("end", end);
            }

            sql.append("  ORDER BY app, uri, ip, timestamp")
                    .append(") AS unique_hits ");
            sql.append("GROUP BY app, uri ");
            sql.append("ORDER BY hits DESC ");
        } else {
            sql.append("SELECT app, uri, COUNT(ip) AS hits FROM endpoint_requests ");

            boolean hasWhere = false;
            if (uris != null && !uris.isEmpty()) {
                sql.append("WHERE uri IN (:uris) ");
                parameters.addValue("uris", uris);
                hasWhere = true;
            }

            if (start != null) {
                sql.append(hasWhere ? "AND " : "WHERE ").append("timestamp >= :start ");
                parameters.addValue("start", start);
                hasWhere = true;
            }

            if (end != null) {
                sql.append(hasWhere ? "AND " : "WHERE ").append("timestamp < :end ");
                parameters.addValue("end", end);
            }

            sql.append("GROUP BY app, uri ");
            sql.append("ORDER BY hits DESC ");
        }

        if (limit != null) {
            sql.append("LIMIT :limit");
            parameters.addValue("limit", limit);
        }

        try {
            return jdbc.query(sql.toString(), parameters, new ViewStatsResultSetMapper());
        } catch (EmptyResultDataAccessException ignored) {
            return List.of();
        }
    }
}
