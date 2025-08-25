package com.co.btg.api.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class UpdateUserRoleRequest {

    @NotBlank(message = "El ID de usuario es obligatorio")
    private String userId;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(USER|ADMIN)$", message = "El rol debe ser USER o ADMIN")
    private String role;
}
