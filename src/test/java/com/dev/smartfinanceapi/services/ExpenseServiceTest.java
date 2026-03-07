package com.dev.smartfinanceapi.services;

import com.dev.smartfinanceapi.dtos.ExpenseRequest;
import com.dev.smartfinanceapi.models.Category;
import com.dev.smartfinanceapi.models.Expense;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.CategoryRepository;
import com.dev.smartfinanceapi.repositories.ExpenseRepository;
import com.dev.smartfinanceapi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    // Clonamos TODOS los repositorios que usa tu servicio
    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    // Inyectamos los clones en tu servicio real
    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void createExpense_ShouldReturnSavedExpense() {
        // 1. ARRANGE: Preparamos los datos
        // Usamos tu DTO de entrada
        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(new BigDecimal("15.50")); // Cambia a Double si en tu entidad usas Double (ej. 15.50)
        request.setDescription("Prueba de Gasto");
        request.setCategoryId(1L);
        request.setUserId(1L);

        // Simulamos un Usuario y una Categoría que ya existen en la BD
        User mockUser = new User();
        mockUser.setId(1L);

        Category mockCategory = new Category();
        mockCategory.setId(1L);

        // El gasto final que la BD "guardaría"
        Expense savedExpense = new Expense();
        savedExpense.setId(1L);
        savedExpense.setAmount(new BigDecimal("15.50"));
        savedExpense.setDescription("Prueba de Gasto");
        savedExpense.setUser(mockUser);
        savedExpense.setCategory(mockCategory);

        // Le damos instrucciones a los clones:
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);

        // 2. ACT: Ejecutamos el método con el DTO
        Expense result = expenseService.createExpense(request);

        // 3. ASSERT: Verificamos que todo haya funcionado
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals(1L, result.getId(), "El ID debe ser 1");
        assertEquals("Prueba de Gasto", result.getDescription());
        assertEquals(1L, result.getUser().getId(), "Debe tener el usuario correcto");

        // Verificamos que el servicio intentó guardar en la BD exactamente una vez
        verify(expenseRepository).save(any(Expense.class));
    }
}