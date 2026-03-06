package com.dev.smartfinanceapi.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Esta es tu firma digital. En producción, esto iría en variables de entorno, no en el código.
    // IMPORTANTE: Debe ser una cadena larga y segura (mínimo 256 bits).
    private static final String SECRET_KEY = "SmartFinanceApiSecretKeySuperSeguraYFuerte12345!";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Guardamos el correo del usuario dentro del token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expira en 24 horas
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Lo firmamos con nuestra llave
                .compact(); // Construimos el token final
    }
}