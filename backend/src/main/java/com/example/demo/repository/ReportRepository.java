package com.example.demo.repository;

import com.example.demo.dto.ReportResponse;
import com.example.demo.dto.ReportRow;
import com.example.demo.dto.ReportStats;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ReportRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public ReportRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional(readOnly = true)
    public ReportResponse run(LocalDate from, LocalDate to, Integer serviceId, Integer severityId) {

        // 1. Build the WHERE clause. Only fixed text goes in the SQL; user values go in
        // params.
        String where = " WHERE i.opened_at >= :from AND i.opened_at < :toExclusive";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("from", from.atStartOfDay())
                .addValue("toExclusive", to.plusDays(1).atStartOfDay()); // makes "to" inclusive

        if (serviceId != null) {
            where += " AND i.service_id = :serviceId";
            params.addValue("serviceId", serviceId);
        }
        if (severityId != null) {
            where += " AND i.severity_id = :severityId";
            params.addValue("severityId", severityId);
        }

        // 2. Statistics
        String statsSql = """
                SELECT COUNT(*) AS total,
                       COUNT(*) FILTER (WHERE i.status <> 'resolved') AS unresolved,
                       ROUND(AVG(EXTRACT(EPOCH FROM (i.resolved_at - i.opened_at)) / 60))::int AS avg_resolve_minutes
                FROM incidents i
                """ + where;

        ReportStats stats = jdbc.queryForObject(statsSql, params, (rs, rowNum) -> new ReportStats(
                rs.getInt("total"),
                rs.getInt("unresolved"),
                rs.getObject("avg_resolve_minutes", Integer.class)));

        // 3. Rows, with names joined in from the lookup tables
        String rowsSql = """
                SELECT i.id, i.title, s.name AS service, sl.name AS severity, e.name AS assignee,
                       i.status, i.opened_at, i.resolved_at
                FROM incidents i
                JOIN services s         ON s.id  = i.service_id
                JOIN severity_levels sl ON sl.id = i.severity_id
                LEFT JOIN engineers e   ON e.id  = i.assignee_id
                """ + where + " ORDER BY i.opened_at DESC";

        List<ReportRow> rows = jdbc.query(rowsSql, params, (rs, rowNum) -> new ReportRow(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("service"),
                rs.getString("severity"),
                rs.getString("assignee"),
                rs.getString("status"),
                rs.getObject("opened_at", LocalDateTime.class),
                rs.getObject("resolved_at", LocalDateTime.class)));

        return new ReportResponse(stats, rows);
    }
}