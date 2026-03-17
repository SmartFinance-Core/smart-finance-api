package com.dev.smartfinanceapi.dtos;

import java.math.BigDecimal;

public class WebhookIncomeRequest {
    private String userEmail;
    private BigDecimal amount;
    private String source; // Ej: "Sueldo BCP", "Yape de Cliente"
    private String externalId;
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    // Getters y Setters
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
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