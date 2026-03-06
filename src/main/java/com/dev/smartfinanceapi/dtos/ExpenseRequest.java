package com.dev.smartfinanceapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ExpenseRequest {

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal amount;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoryId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;
}