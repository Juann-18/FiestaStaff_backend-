package com.backend.fiestaStaff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateWorkerRequest {
    @NotNull(message = "User ID is required")
    private Long idUser;

    @NotBlank(message = "Specialty is required")
    private String specialty;

    public CreateWorkerRequest() {}

    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
