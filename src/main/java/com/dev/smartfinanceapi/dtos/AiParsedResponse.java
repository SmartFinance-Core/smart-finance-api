package com.dev.smartfinanceapi.dtos;

public class AiParsedResponse {
    private Double monto;
    private String categoria;
    private String descripcion_original;

    // Getters y Setters
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getDescripcion_original() { return descripcion_original; }
    public void setDescripcion_original(String descripcion_original) { this.descripcion_original = descripcion_original; }
}