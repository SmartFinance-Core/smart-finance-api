package com.dev.smartfinanceapi.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ej: "Comprar Laptop", "Viaje"

    @Column(nullable = false)
    private BigDecimal targetAmount; // Cuánto dinero necesitas

    @Column(nullable = false)
    private BigDecimal currentAmount = BigDecimal.ZERO; // Cuánto llevas ahorrado (empieza en 0)

    @Column(nullable = false)
    private LocalDate deadline; // Fecha límite para cumplir la meta

    // Relación: Muchas metas pertenecen a un Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}