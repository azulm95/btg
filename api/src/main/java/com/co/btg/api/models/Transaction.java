package com.co.btg.api.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "Transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @DynamoDBHashKey(attributeName = "transactionId")
    private String transactionId; // PK

    @DynamoDBAttribute(attributeName = "userId")
    private String userId; // referencia al usuario

    @DynamoDBAttribute(attributeName = "fundId")
    private String fundId; // referencia al fondo

    @DynamoDBAttribute(attributeName = "type")
    private String type; // SUBSCRIPTION o CANCELLATION

    @DynamoDBAttribute(attributeName = "amount")
    private Double amount;

    @DynamoDBAttribute(attributeName = "date")
    private String date; // ISO-8601 (ej: 2025-08-22T15:30:00)
}
