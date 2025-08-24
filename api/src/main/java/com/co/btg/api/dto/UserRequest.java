package com.co.btg.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

	   private String userId; // PK

	    private String name;

	    private String email;

	    private String phone;

	    private String preferredNotification; // EMAIL o SMS

	    private Double balance; // saldo actual
}
