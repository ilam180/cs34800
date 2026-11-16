package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // "this class maps to a database table"
@Table(name = "engineers") // which table
public class Engineer {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Postgres assigns it (SERIAL)
    private Integer id;

    private String name; // maps to the "name" column automatically

    protected Engineer() {
    } // JPA needs an empty constructor to build objects from rows

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}