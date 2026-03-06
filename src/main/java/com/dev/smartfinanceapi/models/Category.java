package com.dev.smartfinanceapi.models; // Asegúrate de que el paquete sea el tuyo

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Ej: "Alimentación", "Transporte"

    private String description; // Opcional, para dar más detalle
}