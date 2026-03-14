package com.dev.smartfinanceapi.controllers;

import com.dev.smartfinanceapi.dtos.WebhookExpenseRequest;
import com.dev.smartfinanceapi.dtos.WebhookIncomeRequest;
import com.dev.smartfinanceapi.models.Category;
import com.dev.smartfinanceapi.models.Expense;
import com.dev.smartfinanceapi.models.Income;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.CategoryRepository;
import com.dev.smartfinanceapi.repositories.UserRepository;
import com.dev.smartfinanceapi.services.ExpenseService;
import com.dev.smartfinanceapi.services.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    // Esta es la llave secreta que solo Java y n8n conocerán.
    // (En un entorno profesional real, esto va en el application.properties)
    @Value("${n8n.api.key}")
    private String n8nApiKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/n8n/receipt")
    public ResponseEntity<?> receiveReceiptFromN8n(
            @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @RequestBody WebhookExpenseRequest request) {

        // 1. EL GUARDIA VIP: Validamos la llave de seguridad
        if (apiKey == null || !apiKey.equals(n8nApiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado: API Key inválida");
        }

        // 2. Buscamos al dueño del correo (tu cuenta)
        User user = userRepository.findFirstByEmail(request.getUserEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado en la base de datos");
        }

        // 3. Buscamos la categoría enviada por n8n (o asignamos la por defecto si no existe)
        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseGet(() -> categoryRepository.findById(1L).orElseThrow());

        // 4. Armamos el Gasto automáticamente
        Expense newExpense = new Expense();
        newExpense.setAmount(request.getAmount());
        newExpense.setDescription(request.getDescription());
        newExpense.setCategory(category);
        newExpense.setUser(user);
        newExpense.setDate(LocalDateTime.now());

        // 5. Lo guardamos (Y aprovechamos para que tu servicio dispare el evento a RabbitMQ)
        Expense savedExpense = expenseService.createExpense(newExpense);

        System.out.println("✅ [WEBHOOK] Gasto automático registrado desde n8n: S/" + savedExpense.getAmount());

        return ResponseEntity.ok(savedExpense);
    }
    // ... (asegúrate de inyectar el IncomeService arriba si no lo tienes)
    @Autowired
    private IncomeService incomeService;

    // ✨ LA NUEVA PUERTA PARA INGRESOS AUTOMÁTICOS
    @PostMapping("/n8n/income")
    public ResponseEntity<?> receiveIncomeFromN8n(
            @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @RequestBody WebhookIncomeRequest request) {

        // 1. Validamos la misma llave de seguridad
        if (apiKey == null || !apiKey.equals(n8nApiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado: API Key inválida");
        }

        // 2. Buscamos al dueño del correo
        User user = userRepository.findFirstByEmail(request.getUserEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado en la base de datos");
        }

        // 3. Armamos el Ingreso automáticamente
        Income newIncome = new Income();
        newIncome.setAmount(request.getAmount());
        newIncome.setSource(request.getSource());
        newIncome.setUser(user);
        newIncome.setDate(LocalDateTime.now());

        // 4. Lo guardamos en la base de datos
        Income savedIncome = incomeService.createIncome(newIncome);

        System.out.println("💰 [WEBHOOK] Ingreso automático registrado desde n8n: S/" + savedIncome.getAmount());

        return ResponseEntity.ok(savedIncome);
    }
}