package com.co.btg.api.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.co.btg.api.exceptions.UserNotFoundException;

class UserNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "User not found";
        UserNotFoundException exception = new UserNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldThrowAndCatchException() {
        String message = "User not found";

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> { throw new UserNotFoundException(message); }
        );

        assertEquals(message, exception.getMessage());
    }
}