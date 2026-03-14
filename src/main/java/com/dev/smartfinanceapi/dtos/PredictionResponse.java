package com.dev.smartfinanceapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionResponse {

    // Usamos JsonProperty porque Python envía con guion bajo (snake_case)
    // y en Java usamos camelCase.
    @JsonProperty("daily_average")
    private Double dailyAverage;

    @JsonProperty("days_remaining")
    private Integer daysRemaining;

    @JsonProperty("zero_date")
    private String zeroDate;

    private String message;

    // --- GETTERS Y SETTERS ---
    public Double getDailyAverage() { return dailyAverage; }
    public void setDailyAverage(Double dailyAverage) { this.dailyAverage = dailyAverage; }
    public Integer getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(Integer daysRemaining) { this.daysRemaining = daysRemaining; }
    public String getZeroDate() { return zeroDate; }
    public void setZeroDate(String zeroDate) { this.zeroDate = zeroDate; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}