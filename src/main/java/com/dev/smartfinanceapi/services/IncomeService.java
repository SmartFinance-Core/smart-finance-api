package com.dev.smartfinanceapi.services;

import com.dev.smartfinanceapi.models.Income;
import com.dev.smartfinanceapi.repositories.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    // 1. Registrar un nuevo ingreso
    public Income createIncome(Income income) {
        return incomeRepository.save(income);
    }

    // 2. Obtener todo el historial de ingresos de un usuario
    public List<Income> getUserIncomes(Long userId) {
        return incomeRepository.findByUserIdOrderByDateDesc(userId);
    }
}