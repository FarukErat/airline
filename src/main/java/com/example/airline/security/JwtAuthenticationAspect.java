package com.example.airline.security;

import com.example.airline.enums.Role;
import com.example.airline.exception.AuthenticationException;
import com.example.airline.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
public class JwtAuthenticationAspect {

    @Autowired
    private JwtService jwtService;

    @Before("@within(com.example.airline.security.JwtAuthenticated) || @annotation(com.example.airline.security.JwtAuthenticated)")
    public void checkJwtAuthentication(JoinPoint joinPoint) {
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

        List<Role> rolesFromToken = jwtService.extractRoles(token);

        JwtAuthenticated jwtAuthenticated = getJwtAuthenticatedAnnotation(joinPoint);
        if (jwtAuthenticated == null) {
            throw new AuthenticationException("Missing JwtAuthenticated annotation");
        }

        Role[] requiredRoles = jwtAuthenticated.roles();
        if (requiredRoles.length > 0 && !hasRequiredRole(rolesFromToken, requiredRoles)) {
            throw new AuthenticationException("User does not have the required roles");
        }
    }

    private JwtAuthenticated getJwtAuthenticatedAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (method.isAnnotationPresent(JwtAuthenticated.class)) {
            return method.getAnnotation(JwtAuthenticated.class);
        }

        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.isAnnotationPresent(JwtAuthenticated.class)) {
            return declaringClass.getAnnotation(JwtAuthenticated.class);
        }

        return null;
    }

    private boolean hasRequiredRole(List<Role> rolesFromToken, Role[] requiredRoles) {
        for (Role requiredRole : requiredRoles) {
            if (rolesFromToken.contains(requiredRole)) {
                return true; // If user has at least one required role
            }
        }
        return false;
    }
}
