-- CS348 Incident Tracker -- seed data (PostgreSQL)

INSERT INTO severity_levels (id, name, sla_minutes) VALUES
    (1, 'SEV1',   60),     -- 1 hour
    (2, 'SEV2',  240),     -- 4 hours
    (3, 'SEV3', 1440),     -- 1 day
    (4, 'SEV4', 4320);     -- 3 days

INSERT INTO services (name) VALUES
    ('api-gateway'), ('k8s-cluster'), ('checkout-api'), ('billing-worker'),
    ('auth-service'), ('iam-portal'), ('etl-pipeline'), ('analytics-warehouse');

INSERT INTO engineers (name) VALUES
    ('Alex Chen'), ('Priya Patel'), ('Marcus Lee'), ('Sofia Garcia'),
    ('Hannah Kim'), ('Diego Ramirez'), ('Ethan Brooks'), ('Aisha Khan');

-- 600 random incidents over the last 180 days.
-- setseed makes the "random" data the same every time you run this.
-- Change 600 to ~50000 when you want to demo indexes with EXPLAIN in Stage 3.
SELECT setseed(0.348);

INSERT INTO incidents (title, service_id, severity_id, assignee_id, status, opened_at, resolved_at)
SELECT
    title,
    service_id,
    severity_id,
    CASE WHEN status = 'open' THEN NULL ELSE assignee_id END,
    status,
    opened_at,
    CASE WHEN status = 'resolved'
         -- resolve time = between 0.2x and 1.7x the SLA, so some incidents miss their SLA
         THEN LEAST(opened_at + sla_minutes * (0.2 + random() * 1.5) * interval '1 minute', now())
    END
FROM (
    SELECT
        (ARRAY['Elevated 5xx error rate', 'High latency', 'Pods crash looping',
               'Disk almost full', 'Certificate expiring', 'Queue backlog growing',
               'DB connection pool exhausted', 'Bad deploy rolled back'])[1 + floor(random() * 8)::int] AS title,
        1 + floor(random() * 8)::int AS service_id,
        1 + floor(random() * 4)::int AS severity_id,
        1 + floor(random() * 8)::int AS assignee_id,
        opened_at,
        -- anything older than 5 days is resolved; recent ones may still be open / in progress
        CASE WHEN opened_at < now() - interval '5 days' THEN 'resolved'
             ELSE (ARRAY['open', 'in_progress', 'resolved'])[1 + floor(random() * 3)::int]
        END AS status
    FROM (
        SELECT now() - random() * interval '180 days' AS opened_at
        FROM generate_series(1, 600)
    ) AS times
) AS r
JOIN severity_levels sl ON sl.id = r.severity_id
ORDER BY opened_at;
