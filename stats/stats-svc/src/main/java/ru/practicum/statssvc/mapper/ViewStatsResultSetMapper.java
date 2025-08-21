package ru.practicum.statssvc.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.statssvc.model.EndpointStats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewStatsResultSetMapper implements RowMapper<EndpointStats> {
    @Override
    public EndpointStats mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        EndpointStats viewStats = new EndpointStats();
        viewStats.setApp(resultSet.getString("app"));
        viewStats.setUri(resultSet.getString("uri"));
        viewStats.setHits(resultSet.getInt("hits"));
        return viewStats;
    }
}