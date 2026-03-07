package com.dev.smartfinanceapi.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "expenses")
@EntityListeners(AuditingEntityListener.class)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount; // Usamos BigDecimal para dinero, es mucho más preciso que Double

    @Column(nullable = false)
    private String description; // Ej: "Almuerzo en la universidad"

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    // Relación: Muchos gastos pertenecen a un (ManyToOne) Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relación: Muchos gastos pertenecen a una (ManyToOne) Categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}