package com.backend.fiestaStaff.dto;

import com.backend.fiestaStaff.model.WorkerAvailability.WeekDay;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class AvailabilityRequest {
    @NotNull(message = "Week day is required")
    private WeekDay weekDay;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    public AvailabilityRequest() {}

    public WeekDay getWeekDay() { return weekDay; }
    public void setWeekDay(WeekDay weekDay) { this.weekDay = weekDay; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}
