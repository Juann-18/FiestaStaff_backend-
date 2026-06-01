package com.backend.fiestaStaff.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateUserRoleRequest {
    @NotBlank
    private String role;

    public UpdateUserRoleRequest() {}

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
