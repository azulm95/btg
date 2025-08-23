package com.co.btg.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co.btg.api.service.SubscriptionService;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final SubscriptionService subscriptionService;

    // 3. Historial de transacciones por usuario
    @GetMapping("/{userId}")
    public ResponseEntity<?> getHistory(@PathVariable String userId) {
        return ResponseEntity.ok(subscriptionService.getHistory(userId));
    }
}

