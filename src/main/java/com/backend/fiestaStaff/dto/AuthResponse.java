package com.backend.fiestaStaff.dto;

public class AuthResponse {
    private String token;
    private Long id;
    private String role;
    private Long workerId;

    public AuthResponse() {}

    public AuthResponse(String token, Long id, String role, Long workerId) {
        this.token = token;
        this.id = id;
        this.role = role;
        this.workerId = workerId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getWorkerId() { return workerId; }
    public void setWorkerId(Long workerId) { this.workerId = workerId; }
}
