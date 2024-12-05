package com.example.airline.controller;

import com.example.airline.dto.LoginRequest;
import com.example.airline.dto.RegisterRequest;
import com.example.airline.model.UserDetails;
import com.example.airline.service.JwtService;
import com.example.airline.service.UserService;
import com.example.airline.service.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordHasher passwordHasher;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        Optional<UserDetails> existingUser = userService.getUserByUsername(registerRequest.username);

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        String hashedPassword = passwordHasher.hashPassword(registerRequest.password);

        UserDetails newUser = new UserDetails();
        newUser.setUsername(registerRequest.username);
        newUser.setHashedPassword(hashedPassword);

        UserDetails savedUser = userService.saveUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logUserIn(@RequestBody LoginRequest loginRequest) {
        Optional<UserDetails> existingUser = userService.getUserByUsername(loginRequest.username);

        if (existingUser.isPresent()) {
            UserDetails user = existingUser.get();

            if (passwordHasher.verifyPassword(loginRequest.password, user.getHashedPassword())) {
                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                // Prepare the response with the tokens
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);

                return ResponseEntity.ok(tokens);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
