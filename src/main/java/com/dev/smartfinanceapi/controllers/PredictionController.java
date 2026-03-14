package com.dev.smartfinanceapi.controllers;

import com.dev.smartfinanceapi.dtos.PredictionResponse;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.UserRepository;
import com.dev.smartfinanceapi.services.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/burn-rate")
    public ResponseEntity<PredictionResponse> getBurnRate() {
        // Obtenemos tu usuario del Token de Seguridad
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findFirstByEmail(email);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // Llamamos al puente y devolvemos la predicción
        PredictionResponse response = predictionService.getBurnRatePrediction(user.getId());
        return ResponseEntity.ok(response);
    }
}