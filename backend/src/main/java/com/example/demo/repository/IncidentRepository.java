package com.example.demo.repository;

import com.example.demo.model.Incident;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IncidentRepository extends JpaRepository<Incident, Integer> {

    List<Incident> findAllByOrderByOpenedAtDesc(); // newest first

    @Modifying
    @Transactional
    @Query("UPDATE Incident i SET i.assigneeId = :engineerId, i.status = 'in_progress' "
            + "WHERE i.id = :id AND i.assigneeId IS NULL")
    int claim(@Param("id") Integer id, @Param("engineerId") Integer engineerId);
}