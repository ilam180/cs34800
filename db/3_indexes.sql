-- CS348 Incident Tracker -- indexes (PostgreSQL)
-- Kept separate so you can run EXPLAIN ANALYZE before/after for the Stage 3 demo.
-- Note: Postgres does NOT automatically index foreign key columns.

-- Report (Req 2): WHERE opened_at BETWEEN ? AND ?   (every report run filters by date)
CREATE INDEX idx_incidents_opened ON incidents (opened_at);

-- Report filtered to one service: WHERE service_id = ? AND opened_at BETWEEN ? AND ?
CREATE INDEX idx_incidents_service_opened ON incidents (service_id, opened_at);

-- Already indexed by constraints in 01_schema.sql:
--   PRIMARY KEY on every table        -> load / edit / delete one incident by id
--   UNIQUE(name) on lookup tables     -> dropdowns: SELECT id, name ... ORDER BY name
