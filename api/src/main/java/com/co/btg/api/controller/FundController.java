package com.co.btg.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.co.btg.api.models.Fund;
import com.co.btg.api.repositories.FundRepository;


@RestController
@RequestMapping("/api/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundRepository fundRepository;

    @GetMapping
    public ResponseEntity<?> getAllFunds() {
        return ResponseEntity.ok(fundRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createFund(@RequestBody Fund fund) {
        Fund saved = fundRepository.save(fund);
        return ResponseEntity.ok(saved);
    }
}
