package com.dev.smartfinanceapi.controllers;

import com.dev.smartfinanceapi.dtos.AuthResponse;
import com.dev.smartfinanceapi.dtos.LoginRequest;
import com.dev.smartfinanceapi.dtos.RegisterRequest;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().build();
        }

        // Crear el nuevo usuario y encriptar la contraseña
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // Por ahora devolveremos un token simulado.
        // En el siguiente paso implementaremos la generación del token JWT real.
        return ResponseEntity.ok(new AuthResponse("AQUI_IRA_TU_TOKEN_JWT_REAL"));
    }
}