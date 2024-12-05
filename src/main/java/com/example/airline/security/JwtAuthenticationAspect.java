package com.example.airline.security;

import com.example.airline.exception.AuthenticationException;
import com.example.airline.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class JwtAuthenticationAspect {

    @Autowired
    private JwtService jwtService;

    @Before("@within(JwtAuthenticated) || @annotation(JwtAuthenticated)")
    public void checkJwtAuthentication() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new AuthenticationException("Request attributes are not available");
        }

        HttpServletRequest request = attributes.getRequest();

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        if (!jwtService.validateAccessToken(token)) {
            throw new AuthenticationException("Invalid or expired JWT token");
        }
    }
}
