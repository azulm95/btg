package com.co.btg.api.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "El ID de usuario es obligatorio")
    private String userId;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "El número de teléfono debe estar en formato internacional, ej: +573001234567")
    private String phone;

    private Double balance;

    @NotBlank(message = "El tipo de notificación preferido es obligatorio")
    private String preferredNotification;
}

