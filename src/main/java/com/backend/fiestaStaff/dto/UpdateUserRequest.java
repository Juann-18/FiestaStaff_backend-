package com.backend.fiestaStaff.dto;

import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    @Size(max = 100, message = "First name must be at most 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must be at most 100 characters")
    private String lastName;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    public UpdateUserRequest() {}

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
