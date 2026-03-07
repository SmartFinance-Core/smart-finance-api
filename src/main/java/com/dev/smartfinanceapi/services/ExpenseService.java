package com.dev.smartfinanceapi.services;

import com.dev.smartfinanceapi.dtos.ExpenseRequest;
import com.dev.smartfinanceapi.models.Category;
import com.dev.smartfinanceapi.models.Expense;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.CategoryRepository;
import com.dev.smartfinanceapi.repositories.ExpenseRepository;
import com.dev.smartfinanceapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Expense createExpense(ExpenseRequest request) {
        // 1. Buscamos el Usuario y la Categoría en la base de datos
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // 2. Armamos el Gasto
        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setDate(LocalDateTime.now());
        expense.setUser(user);
        expense.setCategory(category);

        // 3. Lo guardamos
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
    public org.springframework.data.domain.Page<Expense> getFilteredExpenses(
            Long userId, Long categoryId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, org.springframework.data.domain.Pageable pageable) {
        return expenseRepository.findExpensesWithFilters(userId, categoryId, startDate, endDate, pageable);
    }
}