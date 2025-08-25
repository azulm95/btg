package com.co.btg.api.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.co.btg.api.models.Transaction;
import com.co.btg.api.service.imp.TransactionService;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getHistory_ShouldReturnUserTransactionHistory() {
        // Arrange
        String userId = "user123";
        List<Transaction> expectedTransactions = Arrays.asList(
            new Transaction(),
            new Transaction()
        );
        when(transactionService.getHistoryByUser(userId))
            .thenReturn(expectedTransactions);

        // Act
        ResponseEntity<?> response = transactionController.getHistory(userId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedTransactions, response.getBody());
        verify(transactionService).getHistoryByUser(userId);
    }

    @Test
    void getHistory_WithEmptyHistory_ShouldReturnEmptyList() {
        // Arrange
        String userId = "user123";
        List<Transaction> emptyList = List.of();
        when(transactionService.getHistoryByUser(userId))
            .thenReturn(emptyList);

        // Act
        ResponseEntity<?> response = transactionController.getHistory(userId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(emptyList, response.getBody());
        verify(transactionService).getHistoryByUser(userId);
    }
}
