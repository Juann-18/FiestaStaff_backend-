package com.backend.fiestaStaff.controller;

import com.backend.fiestaStaff.dto.AvailabilityRequest;
import com.backend.fiestaStaff.model.Event;
import com.backend.fiestaStaff.model.WorkerAvailability;
import com.backend.fiestaStaff.security.CurrentUser;
import com.backend.fiestaStaff.service.AvailabilityService;
import com.backend.fiestaStaff.service.WorkerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final WorkerService workerService;
    private final CurrentUser currentUser;

    public AvailabilityController(AvailabilityService availabilityService, WorkerService workerService,
                                  CurrentUser currentUser) {
        this.availabilityService = availabilityService;
        this.workerService = workerService;
        this.currentUser = currentUser;
    }

    @PostMapping("/api/availability")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerAvailability> createAvailability(
            @Valid @RequestBody AvailabilityRequest request,
            HttpServletRequest httpRequest
    ) {
        Long workerId = currentUser.getWorkerId(httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(availabilityService.createAvailability(request, workerId));
    }

    @PutMapping("/api/availability/{id}")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerAvailability> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityRequest request,
            HttpServletRequest httpRequest
    ) {
        Long workerId = currentUser.getWorkerId(httpRequest);
        return ResponseEntity.ok(availabilityService.updateAvailability(id, request, workerId));
    }

    @GetMapping("/api/my-events")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<List<Event>> getMyEvents(HttpServletRequest httpRequest) {
        Long workerId = currentUser.getWorkerId(httpRequest);
        return ResponseEntity.ok(workerService.getWorkerEvents(workerId));
    }

    @GetMapping("/api/my-availability")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<List<WorkerAvailability>> getMyAvailability(HttpServletRequest httpRequest) {
        Long workerId = currentUser.getWorkerId(httpRequest);
        return ResponseEntity.ok(availabilityService.getAvailabilityByWorker(workerId));
    }
}
