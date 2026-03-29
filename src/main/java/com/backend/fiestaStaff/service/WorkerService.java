package com.backend.fiestaStaff.service;

import com.backend.fiestaStaff.dto.AssignWorkerRequest;
import com.backend.fiestaStaff.dto.CreateWorkerRequest;
import com.backend.fiestaStaff.dto.WorkerStatusRequest;
import com.backend.fiestaStaff.exception.ConflictException;
import com.backend.fiestaStaff.exception.ResourceNotFoundException;
import com.backend.fiestaStaff.model.Event;
import com.backend.fiestaStaff.model.EventWorker;
import com.backend.fiestaStaff.model.Worker;
import com.backend.fiestaStaff.model.WorkerAvailability.WeekDay;
import com.backend.fiestaStaff.repository.EventRepository;
import com.backend.fiestaStaff.repository.EventWorkerRepository;
import com.backend.fiestaStaff.repository.UserRepository;
import com.backend.fiestaStaff.repository.WorkerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventWorkerRepository eventWorkerRepository;

    public WorkerService(WorkerRepository workerRepository, UserRepository userRepository,
                         EventRepository eventRepository, EventWorkerRepository eventWorkerRepository) {
        this.workerRepository = workerRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventWorkerRepository = eventWorkerRepository;
    }

    @Transactional
    public Worker createWorker(CreateWorkerRequest request) {
        if (workerRepository.findByUserId(request.getIdUser()).isPresent()) {
            throw new ConflictException("User is already a worker");
        }

        var user = userRepository.findById(request.getIdUser())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Worker worker = new Worker();
        worker.setUser(user);
        worker.setSpecialty(request.getSpecialty());
        worker.setStatus(Worker.Status.ACTIVE);

        return workerRepository.save(worker);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    @Transactional
    public Worker updateWorkerStatus(Long id, WorkerStatusRequest request) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        worker.setStatus(request.getStatus());
        return workerRepository.save(worker);
    }

    public List<Worker> getAvailableWorkers(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        WeekDay weekDay = WeekDay.valueOf(event.getScheduledAt().getDayOfWeek().name());
        return workerRepository.findAvailableWorkersByWeekDay(weekDay);
    }

    @Transactional
    public EventWorker assignWorkerToEvent(Long eventId, AssignWorkerRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Worker worker = workerRepository.findById(request.getIdWorker())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        if (worker.getStatus() != Worker.Status.ACTIVE) {
            throw new ConflictException("Worker is not active");
        }

        if (eventWorkerRepository.existsByEventIdAndWorkerId(eventId, request.getIdWorker())) {
            throw new ConflictException("Worker is already assigned to this event");
        }

        EventWorker eventWorker = new EventWorker();
        eventWorker.setEvent(event);
        eventWorker.setWorker(worker);

        return eventWorkerRepository.save(eventWorker);
    }

    @Transactional
    public void removeWorkerFromEvent(Long eventId, Long workerId) {
        EventWorker eventWorker = eventWorkerRepository.findByEventIdAndWorkerId(eventId, workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker assignment not found"));

        eventWorkerRepository.delete(eventWorker);
    }

    public List<Event> getWorkerEvents(Long workerId) {
        List<EventWorker> assignments = eventWorkerRepository.findByWorkerId(workerId);
        return assignments.stream()
                .map(EventWorker::getEvent)
                .collect(Collectors.toList());
    }
}
