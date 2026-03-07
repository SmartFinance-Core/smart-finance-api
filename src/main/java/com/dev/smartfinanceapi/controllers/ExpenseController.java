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

    // GET: http://localhost:8080/api/expenses/filter/1?page=0&size=10&categoryId=1
    @GetMapping("/filter/{userId}")
    public ResponseEntity<org.springframework.data.domain.Page<Expense>> getFilteredExpenses(
            @PathVariable Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String startDate, // Formato esperado: 2026-01-01T00:00:00
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy) {

        java.time.LocalDateTime start = startDate != null ? java.time.LocalDateTime.parse(startDate) : null;
        java.time.LocalDateTime end = endDate != null ? java.time.LocalDateTime.parse(endDate) : null;

        // Creamos el objeto de paginación ordenado
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, size, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, sortBy));

        return new ResponseEntity<>(expenseService.getFilteredExpenses(userId, categoryId, start, end, pageable), HttpStatus.OK);
    }

}