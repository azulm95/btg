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
public class User {

    private String userId; // PK

    private String name;

    private String email;

    private String phone;

    private String preferredNotification; // EMAIL o SMS

    private Double balance; // saldo actual
    
    @DynamoDbPartitionKey
    public String getUserId(){
    	return this.userId;
    }
}
