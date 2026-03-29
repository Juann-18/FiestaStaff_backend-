package com.backend.fiestaStaff.repository;

import com.backend.fiestaStaff.model.EventWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventWorkerRepository extends JpaRepository<EventWorker, Long> {
    List<EventWorker> findByEventId(Long eventId);
    List<EventWorker> findByWorkerId(Long workerId);
    Optional<EventWorker> findByEventIdAndWorkerId(Long eventId, Long workerId);
    boolean existsByEventIdAndWorkerId(Long eventId, Long workerId);
}
