package com.co.btg.api.models;



import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@DynamoDBTable(tableName = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @DynamoDBHashKey(attributeName = "userId")
    private String userId; // PK

    @DynamoDBAttribute(attributeName = "name")
    private String name;

    @DynamoDBAttribute(attributeName = "email")
    private String email;

    @DynamoDBAttribute(attributeName = "phone")
    private String phone;

    @DynamoDBAttribute(attributeName = "preferredNotification")
    private String preferredNotification; // EMAIL o SMS

    @DynamoDBAttribute(attributeName = "balance")
    private Double balance; // saldo actual
}
