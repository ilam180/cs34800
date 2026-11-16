package com.example.demo.repository;

import com.example.demo.model.Engineer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EngineerRepository extends JpaRepository<Engineer, Integer> {

    List<Engineer> findAllByOrderByNameAsc();
}