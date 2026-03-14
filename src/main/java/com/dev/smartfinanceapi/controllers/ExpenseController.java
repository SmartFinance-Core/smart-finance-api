package com.dev.smartfinanceapi.controllers;

import com.dev.smartfinanceapi.dtos.TextExpenseRequest;
import com.dev.smartfinanceapi.models.Category;
import com.dev.smartfinanceapi.models.Expense;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.CategoryRepository;
import com.dev.smartfinanceapi.repositories.UserRepository;
import com.dev.smartfinanceapi.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepository;

    // RUTA 1: Obtener la lista para el Dashboard
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable Long userId) {
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        return ResponseEntity.ok(expenses);
    }
    @Autowired private CategoryRepository categoryRepository;
    // RUTA 2: Guardar gasto del Modal manual (CORREGIDA)
    @PostMapping
    public ResponseEntity<Expense> createExpenseManual(@RequestBody java.util.Map<String, Object> payload) {
        // 1. Extraemos los datos del JSON que viene de Angular
        Double amount = Double.parseDouble(payload.get("amount").toString());
        String description = (String) payload.get("description");
        Long categoryId = Long.valueOf(payload.get("categoryId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());

        // 2. Creamos el objeto Expense y seteamos los datos
        Expense expense = new Expense();
        expense.setAmount(java.math.BigDecimal.valueOf(amount));
        expense.setDescription(description);
        expense.setDate(java.time.LocalDateTime.now());

        // 3. Buscamos y asignamos los objetos reales (User y Category) para que MySQL no chille
        User user = userRepository.findById(userId).orElse(null);
        // Necesitas inyectar CategoryRepository arriba para esto:
        Category category = categoryRepository.findById(categoryId).orElse(null);

        expense.setUser(user);
        expense.setCategory(category);

        Expense savedExpense = expenseService.createExpense(expense);
        return ResponseEntity.ok(savedExpense);
    }
    // RUTA 4: Subir imagen de boleta
    @PostMapping("/ai-receipt")
    public ResponseEntity<Expense> uploadReceipt(@RequestParam("file") MultipartFile file) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        if (user == null) return ResponseEntity.status(401).build();

        Expense savedExpense = expenseService.processReceiptImage(file, user.getId());
        return ResponseEntity.ok(savedExpense);
    }
    // RUTA 3: El puente hacia la Inteligencia Artificial
    @PostMapping("/ai-text")
    public ResponseEntity<Expense> createExpenseFromText(@RequestBody TextExpenseRequest request) {
        // 1. ¿Quién está mandando el mensaje? Leemos el correo desde el Token JWT
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Buscamos al usuario en la base de datos
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(401).build(); // No autorizado
        }

        // 3. Enviamos el texto y el ID real del usuario al servicio
        Expense savedExpense = expenseService.processNaturalLanguageExpense(request.getText(), user.getId());

        return ResponseEntity.ok(savedExpense);
    }
}