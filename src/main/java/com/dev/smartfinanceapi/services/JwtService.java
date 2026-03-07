package com.dev.smartfinanceapi.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // Esta es tu firma digital. En producción, esto iría en variables de entorno, no en el código.
    // IMPORTANTE: Debe ser una cadena larga y segura (mínimo 256 bits).
    private static final String SECRET_KEY = "SmartFinanceApiSecretKeySuperSeguraYFuerte12345!";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(Map<String, Object> extraClaims, String email) {
        return Jwts.builder()
                .setClaims(extraClaims) // <-- ¡Aquí inyectamos el ID!
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }
    // Añade esto dentro de JwtService.java

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // El subject es el email que guardamos
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // Si expira o lo modifican, falla y retorna falso
        }
    }
}