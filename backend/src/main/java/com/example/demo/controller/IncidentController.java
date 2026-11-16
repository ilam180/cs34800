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

import com.example.demo.dto.ClaimRequest;
import com.example.demo.dto.IncidentRequest;
import com.example.demo.service.IncidentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentRepository repo;
    private final IncidentService service;

    public IncidentController(IncidentRepository repo, IncidentService service) {
        this.repo = repo;
        this.service = service;
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

    @PostMapping // POST /api/incidents
    @ResponseStatus(HttpStatus.CREATED) // 201
    public Incident create(@RequestBody IncidentRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}") // PUT /api/incidents/42
    public Incident update(@PathVariable("id") Integer id, @RequestBody IncidentRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}") // DELETE /api/incidents/42
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void delete(@PathVariable("id") Integer id) {
        service.delete(id);
    }

    @PostMapping("/{id}/claim") // POST /api/incidents/42/claim
    public void claim(@PathVariable("id") Integer id, @RequestBody ClaimRequest req) {
        service.claim(id, req.engineerId());
    }

}