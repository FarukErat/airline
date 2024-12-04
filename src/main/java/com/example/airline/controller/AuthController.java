package com.example.airline.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api-token-auth")
public class AuthController {

    @PostMapping("/")
    public ResponseEntity<String> createApiToken(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if ("testuser".equals(username) && "testpass".equals(password)) {
            return ResponseEntity.ok("haha");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
