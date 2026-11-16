package com.example.demo.controller;

import com.example.demo.model.MonitoredService;
import com.example.demo.repository.MonitoredServiceRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // methods return data (JSON), not HTML pages
@RequestMapping("/api/services") // base URL for everything in this class
public class MonitoredServiceController {

    private final MonitoredServiceRepository repo;

    // Spring sees this constructor and passes in the repository it built for you
    public MonitoredServiceController(MonitoredServiceRepository repo) {
        this.repo = repo;
    }

    @GetMapping // GET /api/services
    public List<MonitoredService> getAll() {
        return repo.findAllByOrderByNameAsc();
    }
}