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
public class Fund {

    private String fundId; // PK

    private String name;

    private Double minAmount; // monto mínimo de vinculación
    
    private String category;
    
    @DynamoDbPartitionKey
    public String getFundId() {   // <- Aquí debes marcar el getter, no el campo
        return fundId;
    }
}
