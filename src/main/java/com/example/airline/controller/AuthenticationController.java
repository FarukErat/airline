package com.example.airline.controller;

import com.example.airline.dto.LoginRequest;
import com.example.airline.dto.RegisterRequest;
import com.example.airline.model.UserDetails;
import com.example.airline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        Optional<UserDetails> existingUser = userService.getUserByUsername(registerRequest.username);

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        UserDetails newUser = new UserDetails();
        newUser.setUsername(registerRequest.username);
        newUser.setHashedPassword(registerRequest.password);

        UserDetails savedUser = userService.saveUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> logUserIn(@RequestBody LoginRequest loginRequest) {
        Optional<UserDetails> existingUser = userService.getUserByUsername(loginRequest.username);

        if (existingUser.isPresent()) {
            UserDetails user = existingUser.get();
            if (user.getHashedPassword().equals(loginRequest.password)) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}

