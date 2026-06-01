package com.backend.fiestaStaff.repository;

import com.backend.fiestaStaff.model.Worker;
import com.backend.fiestaStaff.model.WorkerAvailability.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByUserId(Long userId);

    @Query("SELECT w FROM Worker w WHERE w.status = 'ACTIVE' AND w.id IN " +
           "(SELECT wa.worker.id FROM WorkerAvailability wa WHERE wa.weekDay = :weekDay)")
    List<Worker> findAvailableWorkersByWeekDay(@Param("weekDay") WeekDay weekDay);

    @Query("SELECT w FROM Worker w WHERE w.status = 'ACTIVE' AND w.id IN " +
           "(SELECT wa.worker.id FROM WorkerAvailability wa WHERE wa.weekDay = :weekDay " +
           "AND wa.startTime <= :startTime AND wa.endTime >= :endTime)")
    List<Worker> findAvailableWorkersByWeekDayAndTime(
            @Param("weekDay") WeekDay weekDay,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
