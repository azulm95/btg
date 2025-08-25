package com.co.btg.api.service.imp;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.co.btg.api.exceptions.TransactionNotFoundException;
import com.co.btg.api.models.Transaction;
import com.co.btg.api.repositories.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getHistoryByUser_WithExistingTransactions_ShouldReturnTransactionList() {
        // Arrange
        String userId = "user123";
        List<Transaction> expectedTransactions = Arrays.asList(
            Transaction.builder()
                .transactionId("tx1")
                .userId(userId)
                .type("OPEN")
                .amount(1000.0)
                .build(),
            Transaction.builder()
                .transactionId("tx2")
                .userId(userId)
                .type("CANCEL")
                .amount(500.0)
                .build()
        );
        
        when(transactionRepository.findByField("userId", userId))
            .thenReturn(expectedTransactions);

        // Act
        List<Transaction> result = transactionService.getHistoryByUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTransactions, result);
        verify(transactionRepository).findByField("userId", userId);
    }

    @Test
    void getHistoryByUser_WithNoTransactions_ShouldThrowException() {
        // Arrange
        String userId = "user123";
        when(transactionRepository.findByField("userId", userId))
            .thenReturn(List.of());

        // Act & Assert
        TransactionNotFoundException exception = assertThrows(
            TransactionNotFoundException.class,
            () -> transactionService.getHistoryByUser(userId)
        );
        
        assertNotNull(exception);
        assertEquals(
            "No se encontraron transacciones para el usuario: " + userId,
            exception.getMessage()
        );
        verify(transactionRepository).findByField("userId", userId);
    }

    @Test
    void getHistoryByUser_ShouldFilterByUserId() {
        // Arrange
        String userId = "user123";
        String otherUserId = "user456";
        List<Transaction> userTransactions = Arrays.asList(
            Transaction.builder()
                .transactionId("tx1")
                .userId(userId)
                .build()
        );
        
        when(transactionRepository.findByField("userId", userId))
            .thenReturn(userTransactions);
        when(transactionRepository.findByField("userId", otherUserId))
            .thenReturn(List.of());

        // Act
        List<Transaction> result = transactionService.getHistoryByUser(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.stream().allMatch(tx -> tx.getUserId().equals(userId)));
        
        // Verify that other user's transactions are not included
        TransactionNotFoundException exception = assertThrows(
            TransactionNotFoundException.class,
            () -> transactionService.getHistoryByUser(otherUserId)
        );
        assertNotNull(exception);
    }
}
