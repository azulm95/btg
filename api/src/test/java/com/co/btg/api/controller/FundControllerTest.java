package com.co.btg.api.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.co.btg.api.models.Fund;
import com.co.btg.api.repositories.FundRepository;


class FundControllerTest {

    @Mock
    private FundRepository fundRepository;

    @InjectMocks
    private FundController fundController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllFunds_ShouldReturnAllFunds() {
        // Arrange
        List<Fund> funds = Arrays.asList(
            new Fund(), 
            new Fund()
        );
        when(fundRepository.findAll()).thenReturn(funds);

        // Act
        ResponseEntity<?> response = fundController.getAllFunds();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(funds, response.getBody());
        verify(fundRepository, times(1)).findAll();
    }

    @Test
    void createFund_ShouldCreateAndReturnFund() {
        // Arrange
        Fund fund = new Fund();
        when(fundRepository.save(any(Fund.class))).thenReturn(fund);

        // Act
        ResponseEntity<?> response = fundController.createFund(fund);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(fund, response.getBody());
        verify(fundRepository, times(1)).save(fund);
    }
}
