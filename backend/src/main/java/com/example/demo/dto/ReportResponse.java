package com.example.demo.dto;

import java.util.List;

public record ReportResponse(ReportStats stats, List<ReportRow> rows) {
}