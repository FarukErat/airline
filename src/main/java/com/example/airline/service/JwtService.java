package com.example.airline.service;

import com.example.airline.enums.TokenType;
import com.example.airline.model.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    final private long accessTokenExpiry = 600000; // 10 minutes
    final private long refreshTokenExpiry = 604800000; // 1 week

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenExpiry, TokenType.access);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenExpiry, TokenType.refresh);
    }

    private String generateToken(UserDetails userDetails, long expiry, TokenType tokenType) {
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .claim("tokenType", tokenType.toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, TokenType.access);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, TokenType.refresh);
    }

    private boolean validateToken(String token, TokenType tokenType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            TokenType tokenTypeFromClaims = TokenType.valueOf(claims.get("tokenType", String.class));

            return !isTokenExpired(claims) && tokenTypeFromClaims.equals(tokenType);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }
}
