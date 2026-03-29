package com.backend.fiestaStaff.controller;

import com.backend.fiestaStaff.dto.AssignWorkerRequest;
import com.backend.fiestaStaff.dto.CreateWorkerRequest;
import com.backend.fiestaStaff.dto.WorkerStatusRequest;
import com.backend.fiestaStaff.model.Worker;
import com.backend.fiestaStaff.service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/workers")
@PreAuthorize("hasRole('ADMIN')")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    public ResponseEntity<List<Worker>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorkers());
    }

    @PostMapping
    public ResponseEntity<Worker> createWorker(@Valid @RequestBody CreateWorkerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workerService.createWorker(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Worker> updateWorkerStatus(
            @PathVariable Long id,
            @Valid @RequestBody WorkerStatusRequest request
    ) {
        return ResponseEntity.ok(workerService.updateWorkerStatus(id, request));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Worker>> getAvailableWorkers(@RequestParam Long event_id) {
        return ResponseEntity.ok(workerService.getAvailableWorkers(event_id));
    }
}
