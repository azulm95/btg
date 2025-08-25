package com.co.btg.api.controller;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import com.co.btg.api.dto.ErrorResponse;
import com.co.btg.api.exceptions.FundNotFoundException;
import com.co.btg.api.exceptions.InsufficientBalanceException;
import com.co.btg.api.exceptions.SubscriptionNotFoundException;
import com.co.btg.api.exceptions.TransactionNotFoundException;
import com.co.btg.api.exceptions.UserNotFoundException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/test");
    }

    @Test
    void handleValidationErrors_shouldReturnBadRequest() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody().getErrors()).containsEntry("field", "must not be null");
    }

    @Test
    void handleTransactionException_shouldReturnBadRequest() {
        TransactionNotFoundException ex = new TransactionNotFoundException("transaction not found");

        ResponseEntity<ErrorResponse> response = handler.handleTransactionException(ex, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("transaction not found");
    }

    @Test
    void handleBalanceException_shouldReturnBadRequest() {
        InsufficientBalanceException ex = new InsufficientBalanceException("balance low");

        ResponseEntity<ErrorResponse> response = handler.handleBalanceException(ex, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("balance low");
    }

    @Test
    void handleFundNotFound_shouldReturnNotFound() {
        FundNotFoundException ex = new FundNotFoundException("fund missing");

        ResponseEntity<ErrorResponse> response = handler.handleFundNotFound(ex, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).isEqualTo("fund missing");
    }

    @Test
    void handleSubscriptionNotFound_shouldReturnNotFound() {
        SubscriptionNotFoundException ex = new SubscriptionNotFoundException("subscription missing");

        ResponseEntity<ErrorResponse> response = handler.handleSubscriptionNotFound(ex, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).isEqualTo("subscription missing");
    }

    @Test
    void handleUserNotFound_shouldReturnNotFound() {
        UserNotFoundException ex = new UserNotFoundException("user missing");

        ResponseEntity<ErrorResponse> response = handler.handleUserNotFound(ex, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).isEqualTo("user missing");
    }

    @Test
    void handleGeneralException_shouldReturnInternalServerError() {
        Exception ex = new Exception("unexpected error");

        ResponseEntity<ErrorResponse> response = handler.handleGeneralException(ex, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody().getMessage()).isEqualTo("unexpected error");
    }
}
