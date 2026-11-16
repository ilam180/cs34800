package com.example.demo.dto;

//avgResolveMinutes null if nothing in range resolved
public record ReportStats(int total, int unresolved, Integer avgResolveMinutes) {
}