package com.dev.smartfinanceapi.controllers;

import com.dev.smartfinanceapi.dtos.ExpenseRequest;
import com.dev.smartfinanceapi.models.Expense;
import com.dev.smartfinanceapi.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*") // Permite que Angular (u otro frontend) se conecte sin bloqueos de CORS
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // POST: http://localhost:8080/api/expenses
// Fíjate en el @Valid que acabamos de agregar:
    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody ExpenseRequest request) {
        Expense newExpense = expenseService.createExpense(request);
        return new ResponseEntity<>(newExpense, HttpStatus.CREATED);
    }

    // GET: http://localhost:8080/api/expenses/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getUserExpenses(@PathVariable Long userId) {
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    // DELETE: http://localhost:8080/api/expenses/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}