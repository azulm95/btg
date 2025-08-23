package com.co.btg.api.dto;

import lombok.Data;

@Data
public class SubscribeRequest {
    private String userId;
    private String fundId;
    private Double amount;
}
