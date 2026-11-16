package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL, so Postgres generates it
    private Integer id;

    private String title;

    // Foreign keys stored as plain ids; the database enforces them (REFERENCES)
    private Integer serviceId;
    private Integer severityId;
    private Integer assigneeId; // null = nobody has claimed it

    private String status; // "open", "in_progress", or "resolved"

    private LocalDateTime openedAt; // TIMESTAMP column -> LocalDateTime
    private LocalDateTime resolvedAt; // null until resolved

    protected Incident() {
    } // for JPA

    // Used when creating a new incident: every new incident starts open,
    // unassigned, opened now
    public Incident(String title, Integer serviceId, Integer severityId) {
        this.title = title;
        this.serviceId = serviceId;
        this.severityId = severityId;
        this.status = "open";
        this.openedAt = LocalDateTime.now();
    }

    // Getters (for JSON)
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public Integer getSeverityId() {
        return severityId;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    // Setters (for editing): only the fields a user is allowed to change
    public void setTitle(String title) {
        this.title = title;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public void setSeverityId(Integer severityId) {
        this.severityId = severityId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}