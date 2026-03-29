package com.backend.fiestaStaff.controller;

import com.backend.fiestaStaff.dto.AssignWorkerRequest;
import com.backend.fiestaStaff.model.EventWorker;
import com.backend.fiestaStaff.service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventWorkerController {

    private final WorkerService workerService;

    public EventWorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping("/{id}/workers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventWorker> assignWorker(
            @PathVariable Long id,
            @Valid @RequestBody AssignWorkerRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workerService.assignWorkerToEvent(id, request));
    }

    @DeleteMapping("/{id}/workers/{workerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeWorker(@PathVariable Long id, @PathVariable Long workerId) {
        workerService.removeWorkerFromEvent(id, workerId);
        return ResponseEntity.noContent().build();
    }
}
