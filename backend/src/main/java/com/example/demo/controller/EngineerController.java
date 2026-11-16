package com.example.demo.controller;

import com.example.demo.model.Engineer;
import com.example.demo.repository.EngineerRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/engineers")
public class EngineerController {

    private final EngineerRepository repo;

    public EngineerController(EngineerRepository repo) {
        this.repo = repo;
    }

    @GetMapping // GET /api/engineers
    public List<Engineer> getAll() {
        return repo.findAllByOrderByNameAsc();
    }
}