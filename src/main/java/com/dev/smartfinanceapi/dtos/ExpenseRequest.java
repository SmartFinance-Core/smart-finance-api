package com.dev.smartfinanceapi.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ExpenseRequest {
    private BigDecimal amount;
    private String description;
    private Long categoryId;
    private Long userId;
}