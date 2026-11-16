package com.example.demo.repository;

import com.example.demo.model.MonitoredService;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoredServiceRepository extends JpaRepository<MonitoredService, Integer> {

    List<MonitoredService> findAllByOrderByNameAsc();
}