package com.backend.fiestaStaff.controller;

import com.backend.fiestaStaff.dto.EventRequest;
import com.backend.fiestaStaff.model.Event;
import com.backend.fiestaStaff.model.User;
import com.backend.fiestaStaff.security.CurrentUser;
import com.backend.fiestaStaff.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final CurrentUser currentUser;

    public EventController(EventService eventService, CurrentUser currentUser) {
        this.eventService = eventService;
        this.currentUser = currentUser;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody EventRequest request) {
        User user = currentUser.get();
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request, user));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Event>> getEvents() {
        User user = currentUser.get();
        return ResponseEntity.ok(eventService.getEventsByUser(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        User user = currentUser.get();
        return ResponseEntity.ok(eventService.getEventById(id, user));
    }
}
