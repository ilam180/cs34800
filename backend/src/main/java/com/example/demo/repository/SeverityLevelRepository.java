package com.example.demo.repository;

import com.example.demo.model.SeverityLevel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeverityLevelRepository extends JpaRepository<SeverityLevel, Integer> {

    List<SeverityLevel> findAllByOrderByIdAsc();
}