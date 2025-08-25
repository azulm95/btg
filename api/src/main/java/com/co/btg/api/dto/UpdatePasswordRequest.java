package com.co.btg.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
	 @NotBlank(message = "El ID de usuario es obligatorio")
    private String userId;
    
	 @NotBlank(message = "La contraseña antigua es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
    private String oldPassword;
    
	 @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
    private String newPassword;
    
   
}
