package com.dev.smartfinanceapi.services;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.dev.smartfinanceapi.dtos.AiParsedResponse;
import com.dev.smartfinanceapi.models.Category;
import com.dev.smartfinanceapi.models.Expense;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.CategoryRepository;
import com.dev.smartfinanceapi.repositories.ExpenseRepository;
import com.dev.smartfinanceapi.repositories.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpEntity;
import org.springframework.core.io.ByteArrayResource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate; // Tu conexión a RabbitMQ

    // 1. Traer gastos por usuario
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    // 2. Crear un gasto manual (Desde el formulario modal de Angular)
    public Expense createExpense(Expense expense) {
        Expense savedExpense = expenseRepository.save(expense);

        // Disparamos el evento asíncrono que ya tenías configurado
        rabbitTemplate.convertAndSend("finance_exchange", "expense_routing_key", "Nuevo gasto registrado: " + savedExpense.getAmount());

        return savedExpense;
    }
    public Expense processReceiptImage(MultipartFile file, Long userId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String pythonUrl = "http://localhost:8000/api/ai/parse-receipt";

            // Configuramos la cabecera para enviar un archivo (MULTIPART)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Empaquetamos el archivo
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename() != null ? file.getOriginalFilename() : "boleta.jpg";
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Disparamos hacia Python
            AiParsedResponse aiResponse = restTemplate.postForObject(pythonUrl, requestEntity, AiParsedResponse.class);

            if (aiResponse == null) throw new RuntimeException("Error en el OCR");

            // Guardamos el gasto en la BD usando la misma lógica que antes
            Category category = categoryRepository.findByName(aiResponse.getCategoria())
                    .orElseGet(() -> categoryRepository.findById(1L).orElseThrow());

            User user = userRepository.findById(userId).orElseThrow();

            Expense newExpense = new Expense();
            newExpense.setAmount(BigDecimal.valueOf(aiResponse.getMonto()));
            newExpense.setDescription(aiResponse.getDescripcion_original());
            newExpense.setCategory(category);
            newExpense.setUser(user);
            newExpense.setDate(LocalDateTime.now());

            Expense savedExpense = expenseRepository.save(newExpense);
            rabbitTemplate.convertAndSend("finance_exchange", "expense_routing_key", "Boleta escaneada por IA: " + savedExpense.getAmount());

            return savedExpense;

        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el archivo de imagen", e);
        }
    }
    // 3. Crear un gasto con Inteligencia Artificial (FastAPI)
    public Expense processNaturalLanguageExpense(String text, Long userId) {
        // A. Preparamos la llamada a Python
        RestTemplate restTemplate = new RestTemplate();
        String pythonUrl = "http://localhost:8000/api/ai/parse-text";

        Map<String, String> request = new HashMap<>();
        request.put("text", text);

        // B. Llamamos a Python y recibimos el JSON
        AiParsedResponse aiResponse = restTemplate.postForObject(pythonUrl, request, AiParsedResponse.class);

        if (aiResponse == null) {
            throw new RuntimeException("El motor de IA no respondió.");
        }

        // C. Buscamos la categoría en MySQL. Si no existe exactamente con ese nombre, asignamos la ID 1 por defecto
        Category category = categoryRepository.findByName(aiResponse.getCategoria())
                .orElseGet(() -> categoryRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Categoría por defecto no encontrada")));

        // D. Buscamos a tu usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // E. Armamos el Gasto
        // E. Armamos el Gasto
        Expense newExpense = new Expense();

        // Convertimos el Double que nos mandó Python al BigDecimal que exige tu base de datos
        newExpense.setAmount(BigDecimal.valueOf(aiResponse.getMonto()));

        newExpense.setDescription(aiResponse.getDescripcion_original());
        newExpense.setCategory(category);
        newExpense.setUser(user);

        // Guardamos la fecha con la HORA EXACTA actual
        newExpense.setDate(LocalDateTime.now());

        // F. Guardamos en BD
        Expense savedExpense = expenseRepository.save(newExpense);

        // G. Disparamos RabbitMQ para que los otros sistemas se enteren
        rabbitTemplate.convertAndSend("finance_exchange", "expense_routing_key", "Nuevo gasto por IA: " + savedExpense.getAmount());

        return savedExpense;
    }
}