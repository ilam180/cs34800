package com.example.demo.controller;

import com.example.demo.model.SeverityLevel;
import com.example.demo.repository.SeverityLevelRepository;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/severities")
public class SeverityLevelController {

    private final SeverityLevelRepository repo;

    // Spring sees this constructor and passes in the repository it built for you
    public SeverityLevelController(SeverityLevelRepository repo) {
        this.repo = repo;
    }

    @GetMapping // GET /api/services
    public List<SeverityLevel> getAll() {
        return repo.findAllByOrderByIdAsc();
    }
}