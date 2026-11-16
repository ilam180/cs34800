package com.example.demo.controller;

import com.example.demo.dto.ReportResponse;
import com.example.demo.repository.ReportRepository;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportRepository reports;

    public ReportController(ReportRepository reports) {
        this.reports = reports;
    }

    // GET
    // /api/reports/incidents?from=2026-10-01&to=2026-11-15&serviceId=3&severityId=1
    @GetMapping("/incidents")
    public ReportResponse incidents(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(value = "serviceId", required = false) Integer serviceId,
            @RequestParam(value = "severityId", required = false) Integer severityId) {
        return reports.run(from, to, serviceId, severityId);
    }
}