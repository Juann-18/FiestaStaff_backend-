package com.backend.fiestaStaff.service;

import com.backend.fiestaStaff.dto.AvailabilityRequest;
import com.backend.fiestaStaff.exception.ForbiddenException;
import com.backend.fiestaStaff.exception.ResourceNotFoundException;
import com.backend.fiestaStaff.model.Worker;
import com.backend.fiestaStaff.model.WorkerAvailability;
import com.backend.fiestaStaff.repository.WorkerAvailabilityRepository;
import com.backend.fiestaStaff.repository.WorkerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AvailabilityService {

    private final WorkerAvailabilityRepository availabilityRepository;
    private final WorkerRepository workerRepository;

    public AvailabilityService(WorkerAvailabilityRepository availabilityRepository,
                              WorkerRepository workerRepository) {
        this.availabilityRepository = availabilityRepository;
        this.workerRepository = workerRepository;
    }

    @Transactional
    public WorkerAvailability createAvailability(AvailabilityRequest request, Long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        if (worker.getStatus() != Worker.Status.ACTIVE) {
            throw new ForbiddenException("Only active workers can manage availability");
        }

        WorkerAvailability availability = new WorkerAvailability();
        availability.setWorker(worker);
        availability.setWeekDay(request.getWeekDay());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());

        return availabilityRepository.save(availability);
    }

    @Transactional
    public WorkerAvailability updateAvailability(Long id, AvailabilityRequest request, Long workerId) {
        WorkerAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found"));

        if (!availability.getWorker().getId().equals(workerId)) {
            throw new ForbiddenException("You can only update your own availability");
        }

        availability.setWeekDay(request.getWeekDay());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());

        return availabilityRepository.save(availability);
    }

    public List<WorkerAvailability> getAvailabilityByWorker(Long workerId) {
        return availabilityRepository.findByWorkerId(workerId);
    }
}
