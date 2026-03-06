package com.dev.smartfinanceapi.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardSummary {
    private BigDecimal totalExpenses;
    private List<CategoryTotal> expensesByCategory;
}