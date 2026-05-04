package com.backend.fiestaStaff.controller;

import com.backend.fiestaStaff.dto.EventRequest;
import com.backend.fiestaStaff.exception.ResourceNotFoundException;
import com.backend.fiestaStaff.model.Event;
import com.backend.fiestaStaff.model.User;
import com.backend.fiestaStaff.repository.UserRepository;
import com.backend.fiestaStaff.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final UserRepository userRepository;

    public EventController(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    private User getUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody EventRequest request, Authentication authentication) {
        User user = getUser(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request, user));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Event>> getEvents(Authentication authentication) {
        User user = getUser(authentication);
        return ResponseEntity.ok(eventService.getEventsByUser(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Event> getEvent(@PathVariable Long id, Authentication authentication) {
        User user = getUser(authentication);
        return ResponseEntity.ok(eventService.getEventById(id, user));
    }
}
