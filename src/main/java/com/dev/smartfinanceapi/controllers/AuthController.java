package com.dev.smartfinanceapi.controllers;

import com.dev.smartfinanceapi.dtos.AuthResponse;
import com.dev.smartfinanceapi.dtos.LoginRequest;
import com.dev.smartfinanceapi.dtos.RegisterRequest;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.UserRepository;
import com.dev.smartfinanceapi.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // RUTA 1: REGISTRO
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Guardamos en MySQL (JPA le asigna el ID automáticamente a la variable 'user')
        userRepository.save(user);

        // 1. Creamos el maletín con el ID real recién creado
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());

        // 2. Generamos el token inyectando el maletín y el correo
        String token = jwtService.generateToken(extraClaims, user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // RUTA 2: INICIO DE SESIÓN
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // 1. Buscamos al usuario por correo
        User user = userRepository.findByEmail(request.getEmail());

        // 2. Si no existe o la contraseña no coincide
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Error 401
        }

        // 3. Creamos el maletín con el ID del usuario
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());

        // 4. Generamos un nuevo token inyectando el ID
        String token = jwtService.generateToken(extraClaims, user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}