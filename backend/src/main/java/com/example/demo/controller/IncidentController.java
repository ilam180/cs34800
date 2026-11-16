package com.example.demo.controller;

import com.example.demo.model.Incident;
import com.example.demo.repository.IncidentRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentRepository repo;

    public IncidentController(IncidentRepository repo) {
        this.repo = repo;
    }

    @GetMapping // GET /api/incidents
    public List<Incident> getAll() {
        return repo.findAllByOrderByOpenedAtDesc();
    }

    @GetMapping("/{id}") // GET /api/incidents/42
    public Incident getOne(@PathVariable("id") Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}