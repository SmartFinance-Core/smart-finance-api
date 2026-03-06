package com.dev.smartfinanceapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Esta anotación convierte a la clase en un vigía que escucha errores en toda la app
public class GlobalExceptionHandler {

    // 1. Capturar errores de validación (cuando @Valid falla)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Extraemos solo el campo que falló y el mensaje limpio que escribimos en el DTO
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Devolvemos un código 400 (Bad Request) con nuestro JSON limpio
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2. Capturar cualquier otro error genérico (ej. "Usuario no encontrado")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}