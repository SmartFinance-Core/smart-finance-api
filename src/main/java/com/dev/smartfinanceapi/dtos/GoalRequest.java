package com.dev.smartfinanceapi.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoalRequest {
    private String name;
    private BigDecimal targetAmount;
    private LocalDate deadline;
    private Long userId;
}