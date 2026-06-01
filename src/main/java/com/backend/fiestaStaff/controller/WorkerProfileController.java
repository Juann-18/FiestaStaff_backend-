package com.backend.fiestaStaff.controller;

import com.backend.fiestaStaff.model.Worker;
import com.backend.fiestaStaff.repository.WorkerRepository;
import com.backend.fiestaStaff.security.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/worker")
public class WorkerProfileController {

    private final CurrentUser currentUser;
    private final WorkerRepository workerRepository;

    public WorkerProfileController(CurrentUser currentUser, WorkerRepository workerRepository) {
        this.currentUser = currentUser;
        this.workerRepository = workerRepository;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Worker> getMyProfile(HttpServletRequest request) {
        Long workerId = currentUser.getWorkerId(request);
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
        return ResponseEntity.ok(worker);
    }
}
