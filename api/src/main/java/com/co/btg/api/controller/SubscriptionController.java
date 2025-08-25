package com.co.btg.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co.btg.api.dto.ApiResponse;
import com.co.btg.api.dto.SubscribeRequest;
import com.co.btg.api.service.imp.SubscriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<?> subscribe(@Valid @RequestBody SubscribeRequest request) {
        
            var subscription = subscriptionService.subscribe(
                    request.getUserId(),
                    request.getFundId(),
                    request.getAmount()
            );
            return ResponseEntity.ok(subscription);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<?> cancel(@PathVariable String subscriptionId) {
            subscriptionService.cancel(subscriptionId);
            return ResponseEntity.ok(new ApiResponse("Suscripción cancelada correctamente"));
    }
    
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getSubscriptionsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUserId(userId));
    }
}

