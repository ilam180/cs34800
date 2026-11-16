DROP TABLE IF EXISTS incident_responders, incidents, severity_levels, services, engineers, teams CASCADE;

-- ---------- Lookup tables (these fill the dropdowns) ----------

CREATE TABLE services (
    id   SERIAL PRIMARY KEY,                   -- eg ecs, eks, etc
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE engineers (
    id   SERIAL PRIMARY KEY,                   -- employees
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE severity_levels (
    id          INT PRIMARY KEY,               -- 1 = catastrophic, 4 = minor
    name        VARCHAR(10) NOT NULL UNIQUE,
    sla_minutes INT NOT NULL                   -- fix within this many minutes
);

CREATE TABLE incidents (
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    service_id  INT NOT NULL REFERENCES services(id),
    severity_id INT NOT NULL REFERENCES severity_levels(id),
    assignee_id INT REFERENCES engineers(id),            -- NULL = nobody has claimed it yet
    status      VARCHAR(20) NOT NULL DEFAULT 'open'
                CHECK (status IN ('open', 'in_progress', 'resolved')),
    opened_at   TIMESTAMP NOT NULL DEFAULT now(),
    resolved_at TIMESTAMP                                -- NULL until resolved
);
