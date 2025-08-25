package com.co.btg.api.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.co.btg.api.exceptions.TransactionNotFoundException;

class TransactionNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Transaction not found";
        TransactionNotFoundException exception = new TransactionNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldThrowAndCatchException() {
        String message = "Transaction not found";

        TransactionNotFoundException exception = assertThrows(
                TransactionNotFoundException.class,
                () -> { throw new TransactionNotFoundException(message); }
        );

        assertEquals(message, exception.getMessage());
    }
}
