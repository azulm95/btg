package com.co.btg.api.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.co.btg.api.models.Fund;
import com.co.btg.api.models.Subscription;
import com.co.btg.api.models.Transaction;
import com.co.btg.api.models.User;
import com.co.btg.api.repositories.FundRepository;
import com.co.btg.api.repositories.GenericRepository;
import com.co.btg.api.repositories.SubscriptionRepository;
import com.co.btg.api.repositories.TransactionRepository;
import com.co.btg.api.repositories.UserRepository;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {

    @Value("${aws.dynamodb.accessKey}")
    private String accessKey;

    @Value("${aws.dynamodb.secretKey}")
    private String secretKey;

    @Value("${aws.dynamodb.endpoint}")
    private String endpoint;

    @Value("${aws.dynamodb.region}")
    private String region;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region)) // <-- ahora lee de application.yml
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient enhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<Fund> fundTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table("Fund", TableSchema.fromBean(Fund.class));
    }

    @Bean
    public FundRepository fundRepository(DynamoDbTable<Fund> fundTable) {
        return new FundRepository(fundTable);
    }

    @Bean
    public DynamoDbTable<Subscription> subscriptionTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table("Subscription", TableSchema.fromBean(Subscription.class));
    }

    @Bean
    public SubscriptionRepository subscriptionRepository(DynamoDbTable<Subscription> subscriptionTable) {
        return new SubscriptionRepository(subscriptionTable);
    }

    @Bean
    public DynamoDbTable<User> userTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table("User", TableSchema.fromBean(User.class));
    }

    @Bean
    public UserRepository userRepository(DynamoDbTable<User> userTable) {
        return new UserRepository(userTable);
    }

    @Bean
    public DynamoDbTable<Transaction> transactionTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table("Transaction", TableSchema.fromBean(Transaction.class));
    }

    @Bean
    public TransactionRepository transactionRepository(DynamoDbTable<Transaction> transactionTable) {
        return new TransactionRepository(transactionTable);
    }
}
