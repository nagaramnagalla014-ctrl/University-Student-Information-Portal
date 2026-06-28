package com.university.portal.dto;

public class LoginRequest {
    private String email;
    private String password;
    private String role; // STUDENT, FACULTY, ADMIN

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
