package com.example.airline.security;

import com.example.airline.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpStatus;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Skip filtering for /api-token-auth/ endpoint
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api-token-auth/")) {
            filterChain.doFilter(request, response); // Skip JWT validation and continue
            return;
        }

        // Extract the Bearer token from the Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

            // Validate the token
            if (!jwtService.validateToken(token)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired JWT token");
                return; // Stop processing and send an unauthorized response
            }
        }

        // Continue the request-response chain
        filterChain.doFilter(request, response);
    }
}
