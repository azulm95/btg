package com.co.btg.api.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "Subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @DynamoDBHashKey(attributeName = "subscriptionId")
    private String subscriptionId; // PK (UUID)

    @DynamoDBAttribute(attributeName = "userId")
    private String userId; // referencia al usuario

    @DynamoDBAttribute(attributeName = "fundId")
    private String fundId; // referencia al fondo

    @DynamoDBAttribute(attributeName = "amount")
    private Double amount; // monto vinculado

    @DynamoDBAttribute(attributeName = "active")
    private Boolean active; // true = vigente, false = cancelada

    @DynamoDBAttribute(attributeName = "startDate")
    private String startDate; // fecha suscripción (ISO-8601)

    @DynamoDBAttribute(attributeName = "endDate")
    private String endDate; // fecha cancelación (opcional, null si sigue activo)
}

