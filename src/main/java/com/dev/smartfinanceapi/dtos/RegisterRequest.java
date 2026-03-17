package com.dev.smartfinanceapi.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;

    @NotBlank(message = "El número de WhatsApp es obligatorio")
    @Pattern(
            regexp = "^51[9][0-9]{8}$",
            message = "El número debe tener formato peruano: 51 seguido de 9 dígitos (ej: 51987654321)"
    )
    private String phoneNumber;
}