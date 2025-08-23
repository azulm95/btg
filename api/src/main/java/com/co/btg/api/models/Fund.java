package com.co.btg.api.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "Funds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fund {

    @DynamoDBHashKey(attributeName = "fundId")
    private String fundId; // PK

    @DynamoDBAttribute(attributeName = "name")
    private String name;

    @DynamoDBAttribute(attributeName = "minAmount")
    private Double minAmount; // monto mínimo de vinculación
}
