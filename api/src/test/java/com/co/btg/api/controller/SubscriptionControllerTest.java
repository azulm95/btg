package com.co.btg.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.co.btg.api.dto.ApiResponse;
import com.co.btg.api.dto.SubscribeRequest;
import com.co.btg.api.models.Subscription;
import com.co.btg.api.service.imp.SubscriptionService;

class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void subscribe_ShouldCreateSubscription() {
        // Arrange
        SubscribeRequest request = new SubscribeRequest();
        request.setUserId("user123");
        request.setFundId("fund123");
        request.setAmount(1000.00);

        Subscription expectedSubscription = new Subscription();
        when(subscriptionService.subscribe(
            request.getUserId(), 
            request.getFundId(), 
            request.getAmount()
        )).thenReturn(expectedSubscription);

        // Act
        ResponseEntity<?> response = subscriptionController.subscribe(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedSubscription, response.getBody());
        verify(subscriptionService).subscribe(
            request.getUserId(), 
            request.getFundId(), 
            request.getAmount()
        );
    }

    @Test
    void cancel_ShouldCancelSubscription() {
        // Arrange
        String subscriptionId = "sub123";
        doNothing().when(subscriptionService).cancel(subscriptionId);

        // Act
        ResponseEntity<?> response = subscriptionController.cancel(subscriptionId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof ApiResponse);
        assertEquals("Suscripción cancelada correctamente", 
            Optional.ofNullable(response.getBody())
                .map(body -> ((ApiResponse)body).getMessage())
                .orElse(""));
        verify(subscriptionService).cancel(subscriptionId);
    }

    @Test
    void getSubscriptionsByUser_ShouldReturnUserSubscriptions() {
        // Arrange
        String userId = "user123";
        List<Subscription> expectedSubscriptions = Arrays.asList(
            new Subscription(),
            new Subscription()
        );
        when(subscriptionService.getSubscriptionsByUserId(userId))
            .thenReturn(expectedSubscriptions);

        // Act
        ResponseEntity<?> response = subscriptionController.getSubscriptionsByUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedSubscriptions, response.getBody());
        verify(subscriptionService).getSubscriptionsByUserId(userId);
    }
}
