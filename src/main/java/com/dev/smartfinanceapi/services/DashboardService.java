package com.dev.smartfinanceapi.services;

import com.dev.smartfinanceapi.dtos.CategoryTotal;
import com.dev.smartfinanceapi.dtos.DashboardSummary;
import com.dev.smartfinanceapi.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public DashboardSummary getSummary(Long userId) {
        DashboardSummary summary = new DashboardSummary();

        // Obtenemos el total (Si no hay gastos, devuelve 0 en lugar de null)
        BigDecimal total = expenseRepository.getTotalExpensesByUserId(userId);
        summary.setTotalExpenses(total != null ? total : BigDecimal.ZERO);

        // Obtenemos el desglose por categorías
        List<CategoryTotal> byCategory = expenseRepository.getTotalExpensesByCategory(userId);
        summary.setExpensesByCategory(byCategory);

        return summary;
    }
}