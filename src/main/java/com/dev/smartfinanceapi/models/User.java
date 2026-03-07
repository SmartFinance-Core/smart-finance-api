package com.dev.smartfinanceapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data // Anotación de Lombok que nos genera getters, setters y constructores invisibles
@Entity // Le dice a Spring Boot: "Esta clase es una tabla de base de datos"
@Table(name = "users") // Le ponemos "users" porque "user" a veces da error en SQL
public class User {

    @Id // Es la llave primaria (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrementable (1, 2, 3...)
    private Long id;

    @Column(nullable = false, unique = true) // No puede estar vacío y no pueden haber correos repetidos
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", updatable = false) // Fecha de creación, no se puede modificar luego
    private LocalDateTime createdAt = LocalDateTime.now();
}