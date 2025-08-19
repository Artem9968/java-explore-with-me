package ru.practicum.statsvc.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.statsvc.model.ViewStats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewStatsRowMapper implements RowMapper<ViewStats> {
    @Override
    public ViewStats mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(resultSet.getString("app"));
        viewStats.setUri(resultSet.getString("uri"));
        viewStats.setHits(resultSet.getInt("hits"));
        return viewStats;
    }
}