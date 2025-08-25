package com.co.btg.api.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.co.btg.api.exceptions.FundNotFoundException;

class FundNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Fund not found";
        FundNotFoundException exception = new FundNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldThrowAndCatchException() {
        String message = "Fund not found";

        FundNotFoundException exception = assertThrows(
                FundNotFoundException.class,
                () -> { throw new FundNotFoundException(message); }
        );

        assertEquals(message, exception.getMessage());
    }
}
