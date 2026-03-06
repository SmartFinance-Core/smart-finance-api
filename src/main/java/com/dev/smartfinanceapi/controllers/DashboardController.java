package com.dev.smartfinanceapi.controllers;

import com.dev.smartfinanceapi.dtos.DashboardSummary;
import com.dev.smartfinanceapi.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Con una sola petición, Angular tendrá todos los datos para dibujar la pantalla principal
    @GetMapping("/summary/{userId}")
    public ResponseEntity<DashboardSummary> getDashboardSummary(@PathVariable Long userId) {
        DashboardSummary summary = dashboardService.getSummary(userId);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}