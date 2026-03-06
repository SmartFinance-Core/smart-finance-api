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

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService; // Inyectamos el nuevo servicio

    // RUTA 1: REGISTRO
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // Generamos el token real
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // RUTA 2: INICIO DE SESIÓN
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // 1. Buscamos al usuario por correo
        User user = userRepository.findByEmail(request.getEmail());

        // 2. Si no existe o la contraseña no coincide (usando el desencriptador)
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Error 401
        }

        // 3. Si todo está bien, generamos un nuevo token
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}