package com.dev.smartfinanceapi.dtos;

import java.math.BigDecimal;

public class WebhookExpenseRequest {
    private String userEmail;     // El correo de donde n8n leyó la boleta (para saber de quién es el gasto)
    private BigDecimal amount;    // El total extraído
    private String description;   // Ej: "Compra en Plaza Vea"
    private String categoryName;  // Ej: "Alimentación"
    private String externalId;
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    // Getters y Setters
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    // Agrega esto a ambos archivos DTO
    private String phoneNumber;

    // Y sus respectivos Getters y Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}