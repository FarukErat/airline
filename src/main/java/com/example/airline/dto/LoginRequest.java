package com.example.airline.dto;

public class LoginRequest {
    final public String username;
    final public String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
