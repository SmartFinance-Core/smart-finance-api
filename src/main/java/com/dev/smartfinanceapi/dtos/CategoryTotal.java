package com.dev.smartfinanceapi.dtos;

import java.math.BigDecimal;

public interface CategoryTotal {
    String getCategoryName();
    BigDecimal getTotalAmount();
}