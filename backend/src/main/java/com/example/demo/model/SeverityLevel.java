package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "severity_levels")
public class SeverityLevel {

    @Id // defined by seed script (4 catastrophic, 1 minimal)
    private Integer id;

    private String name;

    private Integer slaMinutes;

    protected SeverityLevel() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getSlaMinutes() {
        return slaMinutes;
    }
}