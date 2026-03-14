package com.dev.smartfinanceapi.controllers;

import com.dev.smartfinanceapi.models.Income;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.UserRepository;
import com.dev.smartfinanceapi.services.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private UserRepository userRepository;

    // 1. Obtener mis ingresos
    @GetMapping
    public ResponseEntity<List<Income>> getMyIncomes() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findFirstByEmail(email);

        if (user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(incomeService.getUserIncomes(user.getId()));
    }

    // 2. Registrar un ingreso manual
    @PostMapping
    public ResponseEntity<Income> createIncome(@RequestBody Income incomeRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findFirstByEmail(email);

        if (user == null) return ResponseEntity.status(401).build();

        incomeRequest.setUser(user);
        incomeRequest.setDate(LocalDateTime.now());

        Income savedIncome = incomeService.createIncome(incomeRequest);
        return ResponseEntity.ok(savedIncome);
    }
}