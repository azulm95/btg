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
public class Subscription {

    private String subscriptionId; // PK (UUID)

    private String userId; // referencia al usuario

    private String fundId; // referencia al fondo

    private Double amount; // monto vinculado

    private Boolean active; // true = vigente, false = cancelada

    private String startDate; // fecha suscripción (ISO-8601)

    private String endDate; // fecha cancelación (opcional, null si sigue activo)

    @DynamoDbPartitionKey
    public String getSubscriptionId() {
    	return this.subscriptionId;
    }

}

