package com.co.btg.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.co.btg.api.models.User;
import com.co.btg.api.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setBalance(500000.0); // monto inicial
        User saved = userRepository.save(user);
        return ResponseEntity.status(201).body(saved);
    }
}
