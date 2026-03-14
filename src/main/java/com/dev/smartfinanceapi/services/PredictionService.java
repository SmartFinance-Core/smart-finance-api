package com.dev.smartfinanceapi.services;

import com.dev.smartfinanceapi.dtos.PredictionResponse;
import com.dev.smartfinanceapi.models.Expense;
import com.dev.smartfinanceapi.models.Income;
import com.dev.smartfinanceapi.repositories.ExpenseRepository;
import com.dev.smartfinanceapi.repositories.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PredictionService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    public PredictionResponse getBurnRatePrediction(Long userId) {
        // 1. Traemos todo tu historial de la Base de Datos
        List<Income> incomes = incomeRepository.findByUserIdOrderByDateDesc(userId);
        List<Expense> expenses = expenseRepository.findByUserId(userId);

        // 2. Calculamos tu Saldo Actual Exacto
        BigDecimal totalIncome = incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal currentBalance = totalIncome.subtract(totalExpense);

        // 3. Formateamos los gastos al estilo que Python entiende (JSON)
        List<Map<String, Object>> pythonExpenses = expenses.stream().map(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("amount", e.getAmount());
            map.put("date", e.getDate().toString());
            return map;
        }).collect(Collectors.toList());

        // 4. Armamos la caja final (Payload)
        Map<String, Object> payload = new HashMap<>();
        payload.put("current_balance", currentBalance);
        payload.put("expenses", pythonExpenses);

        // 5. ¡El Disparo! Java se comunica con Python
        RestTemplate restTemplate = new RestTemplate();
        String pythonUrl = "http://localhost:8000/api/predict-burn-rate";

        try {
            return restTemplate.postForObject(pythonUrl, payload, PredictionResponse.class);
        } catch (Exception e) {
            System.err.println("⚠️ [ALERTA] No se pudo conectar con el motor de IA en Python: " + e.getMessage());

            // Creamos una respuesta de "contingencia" para no romper la app
            PredictionResponse fallback = new PredictionResponse();
            fallback.setDailyAverage(0.0);
            fallback.setDaysRemaining(0);
            fallback.setZeroDate(null);
            fallback.setMessage("El motor de predicción no está disponible en este momento. Revisa más tarde.");

            return fallback;
        }
    }
}