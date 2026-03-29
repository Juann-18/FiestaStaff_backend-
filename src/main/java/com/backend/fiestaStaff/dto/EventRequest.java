package com.backend.fiestaStaff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EventRequest {
    @NotNull(message = "Event type ID is required")
    private Long idEventType;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    public EventRequest() {}

    public Long getIdEventType() { return idEventType; }
    public void setIdEventType(Long idEventType) { this.idEventType = idEventType; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}
