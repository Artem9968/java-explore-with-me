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
            VALUES ( :app, :uri, :ip, :timestamp)
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
                    generatedKeyHolder, new String[]{"id"}
            );
        } catch (DataAccessException e) {
            throw new InternalServerException("Database save error: " + e.getMessage());
        }

        final Integer hitId = generatedKeyHolder.getKey().intValue();
        hit.setId(hitId);
    }

    @Override
    public List<EndpointStats> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique, Integer limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT app, uri, count(ip) as hits FROM ");

        if (unique) {
            sql.append("(SELECT DISTINCT ON (ip) app, uri, ip, timestamp FROM endpoint_requests) AS unique_hits");

        } else {
            sql.append("endpoint_requests");
        }

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Boolean hasWhereClause = false;

        if (uris != null && !uris.isEmpty()) {
            sql.append(" WHERE uri IN (:uris)");
            parameters.addValue("uris", uris);
            hasWhereClause = true;
        }

        if (start != null) {
            if (hasWhereClause) {
                sql.append(" AND timestamp >= :start");
            } else {
                sql.append(" WHERE timestamp >= :start");
                hasWhereClause = true;
            }
            parameters.addValue("start", start);
        }

        if (end != null) {
            if (hasWhereClause) {
                sql.append(" AND timestamp < :end");
            } else {
                sql.append(" WHERE timestamp < :end");
            }
            parameters.addValue("end", end);
        }

        sql.append(" GROUP BY uri, app ORDER BY hits DESC");

        if (limit != null) {
            parameters.addValue("limit", limit);  // Теперь :limit вместо :size
            sql.append(" LIMIT :limit");
        }

        try {
            List<EndpointStats> stats = jdbc.query(sql.toString(),
                    parameters,
                    new ViewStatsResultSetMapper());
            return stats;
        } catch (EmptyResultDataAccessException ignored) {
            return List.of();
        }
    }
}
