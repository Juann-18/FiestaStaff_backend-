package com.backend.fiestaStaff.dto;

import jakarta.validation.constraints.NotNull;

public class AssignWorkerRequest {
    @NotNull(message = "Worker ID is required")
    private Long idWorker;

    public AssignWorkerRequest() {}

    public Long getIdWorker() { return idWorker; }
    public void setIdWorker(Long idWorker) { this.idWorker = idWorker; }
}
