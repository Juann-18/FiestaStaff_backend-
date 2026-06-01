package com.backend.fiestaStaff.service;

import com.backend.fiestaStaff.dto.EventRequest;
import com.backend.fiestaStaff.exception.ForbiddenException;
import com.backend.fiestaStaff.exception.ResourceNotFoundException;
import com.backend.fiestaStaff.model.Event;
import com.backend.fiestaStaff.model.EventType;
import com.backend.fiestaStaff.model.User;
import com.backend.fiestaStaff.repository.EventRepository;
import com.backend.fiestaStaff.repository.EventTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EmailService emailService;

    public EventService(EventRepository eventRepository, EventTypeRepository eventTypeRepository,
                         EmailService emailService) {
        this.eventRepository = eventRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Event createEvent(EventRequest request, User user) {
        EventType eventType = eventTypeRepository.findById(request.getIdEventType())
                .orElseThrow(() -> new ResourceNotFoundException("Event type not found"));

        Event event = new Event();
        event.setUser(user);
        event.setEventType(eventType);
        event.setLocation(request.getLocation());
        event.setScheduledAt(request.getScheduledAt());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());

        Event savedEvent = eventRepository.save(event);
        emailService.sendEventConfirmation(savedEvent, user);
        return savedEvent;
    }

    public List<Event> getEventsByUser(User user) {
        return eventRepository.findByUserId(user.getId());
    }

    public Event getEventById(Long id, User user) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        boolean isAdmin = user.getRole() == User.Role.ADMIN;
        boolean isOwner = event.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You don't have access to this event");
        }

        return event;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional
    public Event updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        EventType eventType = eventTypeRepository.findById(request.getIdEventType())
                .orElseThrow(() -> new ResourceNotFoundException("Event type not found"));

        event.setEventType(eventType);
        event.setLocation(request.getLocation());
        event.setScheduledAt(request.getScheduledAt());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found");
        }
        eventRepository.deleteById(id);
    }
}
