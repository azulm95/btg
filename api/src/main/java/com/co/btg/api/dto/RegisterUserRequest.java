package com.co.btg.api.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class RegisterUserRequest {

    private String userId; // opcional, se genera si no viene

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "El número de teléfono debe estar en formato internacional, ej: +573001234567")
    private String phone;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
    private String password;

    private Double balance; // se setea por defecto en el controller

    @NotBlank(message = "El tipo de notificación preferido es obligatorio")
    private String preferredNotification;
}
