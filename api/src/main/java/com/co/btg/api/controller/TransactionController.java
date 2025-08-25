package com.co.btg.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co.btg.api.service.imp.SubscriptionService;
import com.co.btg.api.service.imp.TransactionService;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getHistory(@PathVariable String userId) {
        return ResponseEntity.ok(transactionService.getHistoryByUser(userId));
    }
}

