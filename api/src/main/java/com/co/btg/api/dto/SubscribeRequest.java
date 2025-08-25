package com.co.btg.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SubscribeRequest {
	 @NotBlank(message = "El userId es obligatorio")
	    private String userId;

	    @NotBlank(message = "El fundId es obligatorio")
	    private String fundId;

	    @NotNull(message = "El monto es obligatorio")
	    @Positive(message = "El monto debe ser mayor que 0")
	    private Double amount;
}
