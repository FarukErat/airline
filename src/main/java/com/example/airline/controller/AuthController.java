package com.example.airline.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api-token-auth")
public class AuthController {

    private static final String SECRET_KEY = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @PostMapping("/")
    public ResponseEntity<String> createApiToken(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        // Validate username and password
        if ("testuser".equals(username) && "testpass".equals(password)) {
            // Generate JWT token
            String token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 600000)) // 10 minutes
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();

            return ResponseEntity.ok(token);
        }

        // Unauthorized response
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
