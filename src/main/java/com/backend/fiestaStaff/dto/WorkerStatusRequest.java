package com.backend.fiestaStaff.dto;

import com.backend.fiestaStaff.model.Worker.Status;
import jakarta.validation.constraints.NotNull;

public class WorkerStatusRequest {
    @NotNull(message = "Status is required")
    private Status status;

    public WorkerStatusRequest() {}

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
