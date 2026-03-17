package com.dev.smartfinanceapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    // Recuerda agregar sus respectivos Getters y Setters al final del archivo
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}