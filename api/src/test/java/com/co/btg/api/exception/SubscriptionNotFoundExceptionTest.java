package com.co.btg.api.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.co.btg.api.exceptions.SubscriptionNotFoundException;

class SubscriptionNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Subscription not found";
        SubscriptionNotFoundException exception = new SubscriptionNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldThrowAndCatchException() {
        String message = "Subscription not found";

        SubscriptionNotFoundException exception = assertThrows(
                SubscriptionNotFoundException.class,
                () -> { throw new SubscriptionNotFoundException(message); }
        );

        assertEquals(message, exception.getMessage());
    }
}