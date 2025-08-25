package com.co.btg.api.service.imp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.co.btg.api.enums.NotificationType;
import com.co.btg.api.exceptions.InsufficientBalanceException;
import com.co.btg.api.exceptions.SubscriptionNotFoundException;
import com.co.btg.api.models.Fund;
import com.co.btg.api.models.Subscription;
import com.co.btg.api.models.Transaction;
import com.co.btg.api.models.User;
import com.co.btg.api.repositories.FundRepository;
import com.co.btg.api.repositories.SubscriptionRepository;
import com.co.btg.api.repositories.TransactionRepository;
import com.co.btg.api.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private FundRepository fundRepository;
    
    @Mock
    private SubscriptionRepository subscriptionRepository;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private NotificationFactory notificationFactory;
    
    @Mock
    private NotificationService emailNotificationService;
    
    @InjectMocks
    private SubscriptionService subscriptionService;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    private User testUser;
    private Fund testFund;
    private Subscription testSubscription;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId("user123")
                .name("Test User")
                .email("test@example.com")
                .phone("+1234567890")
                .balance(1000.0)
                .preferredNotification("EMAIL")
                .build();

        testFund = Fund.builder()
                .fundId("fund123")
                .name("Test Fund")
                .minAmount(100.0)
                .build();

        testSubscription = Subscription.builder()
                .subscriptionId("sub123")
                .userId(testUser.getUserId())
                .fundId(testFund.getFundId())
                .amount(500.0)
                .active(true)
                .build();
    }

    @Test
    void subscribe_SuccessfulSubscription_ShouldCreateSubscriptionAndNotify() {
        // Arrange
        Double amount = 500.0;
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(fundRepository.findById(anyString())).thenReturn(Optional.of(testFund));
        when(notificationFactory.getService(NotificationType.EMAIL)).thenReturn(emailNotificationService);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(testSubscription);

        // Act
        Subscription result = subscriptionService.subscribe(testUser.getUserId(), testFund.getFundId(), amount);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testFund.getFundId(), result.getFundId());
        assertEquals(amount, result.getAmount());
        assertTrue(result.getActive());

        verify(userRepository).save(argThat(user -> user.getBalance() == 500.0));
        verify(transactionRepository).save(transactionCaptor.capture());
        verify(emailNotificationService).notifyUser(
            eq(testUser.getEmail()),
            anyString(),
            contains("Se ha suscrito exitosamente al fondo")
        );

        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals("OPEN", savedTransaction.getType());
        assertEquals(amount, savedTransaction.getAmount());
    }

    @Test
    void subscribe_InsufficientBalance_ShouldThrowException() {
        // Arrange
        testUser.setBalance(50.0);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(fundRepository.findById(anyString())).thenReturn(Optional.of(testFund));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () ->
            subscriptionService.subscribe(testUser.getUserId(), testFund.getFundId(), 100.0)
        );

        verify(subscriptionRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void subscribe_BelowMinimumAmount_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(fundRepository.findById(anyString())).thenReturn(Optional.of(testFund));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () ->
            subscriptionService.subscribe(testUser.getUserId(), testFund.getFundId(), 50.0)
        );

        verify(subscriptionRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void cancel_SuccessfulCancellation_ShouldCancelSubscriptionAndRefund() {
        // Arrange
        when(subscriptionRepository.findById(anyString()))
            .thenReturn(Optional.of(testSubscription));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(fundRepository.findById(anyString())).thenReturn(Optional.of(testFund));
        when(notificationFactory.getService(NotificationType.EMAIL)).thenReturn(emailNotificationService);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(testSubscription);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        subscriptionService.cancel(testSubscription.getSubscriptionId());

        // Assert
        verify(subscriptionRepository).save(argThat(sub -> !sub.getActive()));
        verify(userRepository).save(argThat(user -> user.getBalance() == 1500.0));
        verify(transactionRepository).save(transactionCaptor.capture());
        
        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals("CANCEL", savedTransaction.getType());
        assertEquals(testSubscription.getAmount(), savedTransaction.getAmount());
    }

    @Test
    void cancel_InactiveSubscription_ShouldThrowException() {
        // Arrange
        testSubscription.setActive(false);
        when(subscriptionRepository.findById(testSubscription.getSubscriptionId()))
            .thenReturn(Optional.of(testSubscription));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            subscriptionService.cancel(testSubscription.getSubscriptionId())
        );
        assertNotNull(exception);

        verify(userRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void getSubscriptionsByUserId_ShouldReturnUserSubscriptions() {
        // Arrange
        List<Subscription> subscriptions = Arrays.asList(testSubscription);
        when(subscriptionRepository.findByField(eq("userId"), anyString()))
            .thenReturn(subscriptions);

        // Act
        List<Subscription> result = subscriptionService.getSubscriptionsByUserId(testUser.getUserId());

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(testSubscription, result.get(0));
    }

    @Test
    void getSubscriptionsByUserId_NoSubscriptions_ShouldThrowException() {
        // Arrange
        when(subscriptionRepository.findByField("userId", testUser.getUserId()))
            .thenReturn(List.of());

        // Act & Assert
        SubscriptionNotFoundException exception = assertThrows(SubscriptionNotFoundException.class, () ->
            subscriptionService.getSubscriptionsByUserId(testUser.getUserId())
        );
        assertNotNull(exception);
    }
}
