package com.co.btg.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.co.btg.api.dto.UserRequest;
import com.co.btg.api.models.User;
import com.co.btg.api.repositories.GenericRepository;
import com.co.btg.api.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
    	return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest user) {
        user.setBalance(500000.0); // monto inicial
        User saved = userService.saveUser(user);
        return ResponseEntity.status(201).body(saved);
    }
}
