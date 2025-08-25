package com.co.btg.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.co.btg.api.models.Fund;
import com.co.btg.api.repositories.FundRepository;



@RestController
@RequestMapping("/api/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundRepository fundRepository;

    
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllFunds() {
        return ResponseEntity.ok(fundRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createFund(@RequestBody Fund fund) {
        Fund saved = fundRepository.save(fund);
        return ResponseEntity.ok(saved);
    }
}
