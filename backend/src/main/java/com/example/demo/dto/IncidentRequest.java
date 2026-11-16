package com.example.demo.dto;

//json body for create/edit, e.g. {"title":"...", "serviceId":3, "severityId":2}
public record IncidentRequest(String title,
        Integer serviceId,
        Integer severityId,
        Integer assigneeId,
        String status) {
}