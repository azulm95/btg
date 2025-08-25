package com.co.btg.api.controller;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.co.btg.api.dto.RegisterUserRequest;
import com.co.btg.api.dto.UpdatePasswordRequest;
import com.co.btg.api.dto.UpdateUserRequest;
import com.co.btg.api.dto.UpdateUserRoleRequest;
import com.co.btg.api.dto.UserResponse;
import com.co.btg.api.service.imp.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ Obtener usuario por ID (protegido)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.toResponse(userService.findById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody RegisterUserRequest user) {
        user.setBalance(500000.0); // monto inicial
        UserResponse saved = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest user) {
        UserResponse saved = userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role")
    public ResponseEntity<UserResponse> updateUserRole(@Valid @RequestBody UpdateUserRoleRequest dto) {
        UserResponse saved = userService.updateUserRole(dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(saved);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest dto) {
        userService.updatePassword(dto);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    // ✅ Obtener todos los usuarios (solo admin)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(userService.findAll());
    }
}
