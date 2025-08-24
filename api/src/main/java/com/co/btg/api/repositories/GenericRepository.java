package com.co.btg.api.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.enhanced.dynamodb.Key;

public class GenericRepository<T> {

    private final DynamoDbTable<T> table;

    public GenericRepository(DynamoDbTable<T> table) {
        this.table = table;
    }

    public T save(T entity) {
       table.putItem(entity);
       return entity;
    }

    public Optional<T> findById(String id) {
    	  T item = table.getItem(r -> r.key(Key.builder().partitionValue(id).build()));
          return Optional.ofNullable(item);
    }

    public List<T> findAll() {
    	List<T> results = new ArrayList<>();
        table.scan().items().forEach(results::add);
        return results;
    }

    public void delete(String partitionKey) {
        table.deleteItem(r -> r.key(Key.builder().partitionValue(partitionKey).build()));
    }
    
    public List<T> findByField(String fieldName, String value) {
        Expression filter = Expression.builder()
                .expression(fieldName + " = :val")
                .putExpressionValue(":val", AttributeValue.builder().s(value).build())
                .build();

        return table.scan(r -> r.filterExpression(filter))
                .items()
                .stream()
                .collect(Collectors.toList());
    }
    
    public Optional<T> findOneByField(String fieldName, String value) {
        Expression filter = Expression.builder()
                .expression(fieldName + " = :val")
                .putExpressionValue(":val", AttributeValue.builder().s(value).build())
                .build();

        return table.scan(r -> r.filterExpression(filter))
                .items()
                .stream()
                .findFirst(); // Devuelve el primero que encuentre
    }
}
