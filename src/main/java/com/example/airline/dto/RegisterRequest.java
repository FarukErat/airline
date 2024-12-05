package com.example.airline.dto;

public class RegisterRequest {
    final public String username;
    final public String password;

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
