package com.dev.smartfinanceapi.services;

import com.dev.smartfinanceapi.models.Expense;
import com.dev.smartfinanceapi.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Crear un nuevo gasto
    public Expense createExpense(Expense expense) {
        // Aquí en el futuro llamaremos a Python/RabbitMQ para el modelo de Machine Learning
        return expenseRepository.save(expense);
    }

    // Obtener todos los gastos de un usuario
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    // Eliminar un gasto
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}