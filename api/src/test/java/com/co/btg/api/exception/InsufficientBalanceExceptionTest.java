package com.co.btg.api.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.co.btg.api.exceptions.InsufficientBalanceException;

class InsufficientBalanceExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Insufficient balance";
        InsufficientBalanceException exception = new InsufficientBalanceException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldThrowAndCatchException() {
        String message = "Insufficient balance";

        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> { throw new InsufficientBalanceException(message); }
        );

        assertEquals(message, exception.getMessage());
    }
}
