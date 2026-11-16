package com.example.demo.dto;

import java.time.LocalDateTime;

public record ReportRow(Integer id, String title, String service, String severity,
        String assignee, String status,
        LocalDateTime openedAt, LocalDateTime resolvedAt) {
}