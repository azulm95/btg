package com.co.btg.api.config;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.co.btg.api.models.Fund;
import com.co.btg.api.models.Subscription;
import com.co.btg.api.models.Transaction;
import com.co.btg.api.models.User;
import com.co.btg.api.repositories.FundRepository;
import com.co.btg.api.repositories.SubscriptionRepository;
import com.co.btg.api.repositories.TransactionRepository;
import com.co.btg.api.repositories.UserRepository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@ExtendWith(MockitoExtension.class)
class DynamoDbConfigTest {

    @InjectMocks
    private DynamoDbConfig dynamoDbConfig;

    private final String TEST_ACCESS_KEY = "test-access-key";
    private final String TEST_SECRET_KEY = "test-secret-key";
    private final String TEST_ENDPOINT = "http://localhost:8000";
    private final String TEST_REGION = "us-east-1";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(dynamoDbConfig, "accessKey", TEST_ACCESS_KEY);
        ReflectionTestUtils.setField(dynamoDbConfig, "secretKey", TEST_SECRET_KEY);
        ReflectionTestUtils.setField(dynamoDbConfig, "endpoint", TEST_ENDPOINT);
        ReflectionTestUtils.setField(dynamoDbConfig, "region", TEST_REGION);
    }

    @Test
    void dynamoDbClient_ShouldCreateClientWithCorrectConfiguration() {
        // Act
        DynamoDbClient client = dynamoDbConfig.dynamoDbClient();

        // Assert
        assertNotNull(client);
        assertEquals(URI.create(TEST_ENDPOINT), client.serviceClientConfiguration().endpointOverride().get());
        assertEquals(Region.of(TEST_REGION), client.serviceClientConfiguration().region());
    }

    @Test
    void enhancedClient_ShouldCreateEnhancedClientWithDynamoDbClient() {
        // Arrange
        DynamoDbClient mockDynamoDbClient = mock(DynamoDbClient.class);

        // Act
        DynamoDbEnhancedClient enhancedClient = dynamoDbConfig.enhancedClient(mockDynamoDbClient);

        // Assert
        assertNotNull(enhancedClient);
    }

    @Test
    void fundTable_ShouldCreateTableWithCorrectName() {
        // Arrange
        DynamoDbEnhancedClient mockEnhancedClient = mock(DynamoDbEnhancedClient.class);

        // Act
        dynamoDbConfig.fundTable(mockEnhancedClient);

        // Assert
        verify(mockEnhancedClient).table(eq("Fund"), any());
    }

    @Test
    void fundRepository_ShouldCreateRepository() {
        // Arrange
        @SuppressWarnings("unchecked")
        DynamoDbTable<Fund> mockTable = mock(DynamoDbTable.class);

        // Act
        FundRepository repository = dynamoDbConfig.fundRepository(mockTable);

        // Assert
        assertNotNull(repository);
    }

    @Test
    void subscriptionTable_ShouldCreateTableWithCorrectName() {
        // Arrange
        DynamoDbEnhancedClient mockEnhancedClient = mock(DynamoDbEnhancedClient.class);

        // Act
        dynamoDbConfig.subscriptionTable(mockEnhancedClient);

        // Assert
        verify(mockEnhancedClient).table(eq("Subscription"), any());
    }

    @Test
    void subscriptionRepository_ShouldCreateRepository() {
        // Arrange
        @SuppressWarnings("unchecked")
        DynamoDbTable<Subscription> mockTable = mock(DynamoDbTable.class);

        // Act
        SubscriptionRepository repository = dynamoDbConfig.subscriptionRepository(mockTable);

        // Assert
        assertNotNull(repository);
    }

    @Test
    void userTable_ShouldCreateTableWithCorrectName() {
        // Arrange
        DynamoDbEnhancedClient mockEnhancedClient = mock(DynamoDbEnhancedClient.class);

        // Act
        dynamoDbConfig.userTable(mockEnhancedClient);

        // Assert
        verify(mockEnhancedClient).table(eq("User"), any());
    }

    @Test
    void userRepository_ShouldCreateRepository() {
        // Arrange
        @SuppressWarnings("unchecked")
        DynamoDbTable<User> mockTable = mock(DynamoDbTable.class);

        // Act
        UserRepository repository = dynamoDbConfig.userRepository(mockTable);

        // Assert
        assertNotNull(repository);
    }

    @Test
    void transactionTable_ShouldCreateTableWithCorrectName() {
        // Arrange
        DynamoDbEnhancedClient mockEnhancedClient = mock(DynamoDbEnhancedClient.class);

        // Act
        dynamoDbConfig.transactionTable(mockEnhancedClient);

        // Assert
        verify(mockEnhancedClient).table(eq("Transaction"), any());
    }

    @Test
    void transactionRepository_ShouldCreateRepository() {
        // Arrange
        @SuppressWarnings("unchecked")
        DynamoDbTable<Transaction> mockTable = mock(DynamoDbTable.class);

        // Act
        TransactionRepository repository = dynamoDbConfig.transactionRepository(mockTable);

        // Assert
        assertNotNull(repository);
    }
}
