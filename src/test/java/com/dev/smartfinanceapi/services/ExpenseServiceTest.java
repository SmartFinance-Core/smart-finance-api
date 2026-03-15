package com.dev.smartfinanceapi.services;

import com.dev.smartfinanceapi.models.Category;
import com.dev.smartfinanceapi.models.Expense;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.CategoryRepository;
import com.dev.smartfinanceapi.repositories.ExpenseRepository;
import com.dev.smartfinanceapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    @DisplayName("createExpense: debe guardar el gasto y publicar evento en RabbitMQ")
    void createExpense_ShouldSaveAndPublishEvent() {
        User mockUser = new User();
        mockUser.setId(1L);

        Category mockCategory = new Category();
        mockCategory.setId(1L);
        mockCategory.setName("Alimentación");

        Expense inputExpense = new Expense();
        inputExpense.setAmount(new BigDecimal("15.50"));
        inputExpense.setDescription("Almuerzo universitario");
        inputExpense.setUser(mockUser);
        inputExpense.setCategory(mockCategory);

        Expense savedExpense = new Expense();
        savedExpense.setId(1L);
        savedExpense.setAmount(new BigDecimal("15.50"));
        savedExpense.setDescription("Almuerzo universitario");
        savedExpense.setUser(mockUser);
        savedExpense.setCategory(mockCategory);

        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        Expense result = expenseService.createExpense(inputExpense);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("15.50"), result.getAmount());
        assertEquals("Almuerzo universitario", result.getDescription());
        assertEquals(1L, result.getUser().getId());

        verify(expenseRepository, times(1)).save(any(Expense.class));
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("createExpense: si el repository falla, no debe disparar RabbitMQ")
    void createExpense_WhenRepositoryFails_ShouldThrowWithoutPublishing() {
        Expense inputExpense = new Expense();
        inputExpense.setAmount(new BigDecimal("50.00"));
        inputExpense.setDescription("Gasto que fallará");

        when(expenseRepository.save(any(Expense.class)))
                .thenThrow(new RuntimeException("DB connection lost"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> expenseService.createExpense(inputExpense)
        );
        assertEquals("DB connection lost", ex.getMessage());

        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
    }
}
