package com.backend.fiestaStaff.repository;

import com.backend.fiestaStaff.model.WorkerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkerAvailabilityRepository extends JpaRepository<WorkerAvailability, Long> {
    @Query("SELECT wa FROM WorkerAvailability wa WHERE wa.worker.id = :workerId")
    List<WorkerAvailability> findByWorkerId(@Param("workerId") Long workerId);
}
