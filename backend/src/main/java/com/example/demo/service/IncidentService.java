package com.example.demo.service;

import com.example.demo.dto.IncidentRequest;
import com.example.demo.model.Incident;
import com.example.demo.repository.IncidentRepository;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service // Spring creates one of these and injects it where needed
public class IncidentService {

    private static final Set<String> STATUSES = Set.of("open", "in_progress", "resolved");

    private final IncidentRepository repo;

    public IncidentService(IncidentRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Incident create(IncidentRequest req) {
        requireFields(req);
        Incident incident = new Incident(req.title(), req.serviceId(), req.severityId());
        return repo.save(incident); // INSERT
    }

    @Transactional
    public Incident update(Integer id, IncidentRequest req) {
        requireFields(req);
        Incident incident = findOrThrow(id);

        incident.setTitle(req.title());
        incident.setServiceId(req.serviceId());
        incident.setSeverityId(req.severityId());
        incident.setAssigneeId(req.assigneeId());

        if (req.status() != null) {
            if (!STATUSES.contains(req.status())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");
            }
            incident.setStatus(req.status());
        }

        // Keep resolved_at consistent with status
        if (incident.getStatus().equals("resolved")) {
            if (incident.getResolvedAt() == null) {
                incident.setResolvedAt(LocalDateTime.now());
            }
        } else {
            incident.setResolvedAt(null);
        }

        return repo.save(incident); // UPDATE
    }

    @Transactional
    public void delete(Integer id) {
        findOrThrow(id);
        repo.deleteById(id); // DELETE
    }

    public void claim(Integer id, Integer engineerId) {
        findOrThrow(id);
        int updated = repo.claim(id, engineerId);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Incident already claimed");
        }
    }

    // ---------- helpers ----------

    private Incident findOrThrow(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void requireFields(IncidentRequest req) {
        if (req.title() == null || req.title().isBlank()
                || req.serviceId() == null || req.severityId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "title, serviceId, and severityId are required");
        }
    }
}