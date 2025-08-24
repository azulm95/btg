package com.co.btg.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    private String transactionId; // PK

    private String userId; // referencia al usuario

    private String fundId; // referencia al fondo

    private String type; // SUBSCRIPTION o CANCELLATION

    private Double amount;

    private String date; // ISO-8601 (ej: 2025-08-22T15:30:00)
    
    @DynamoDbPartitionKey
    public String getTransactionId() {
    	return this.transactionId;
    }
}
