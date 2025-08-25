package com.co.btg.api.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.co.btg.api.models.User;
import com.co.btg.api.repositories.GenericRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final GenericRepository<User> userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {
        String adminId = "1";

        User admin = userRepository.findById(adminId).orElse(new User());
        admin.setUserId(adminId);
        admin.setName("carlos ramirez");
        admin.setEmail("azulm95@gmail.com");
        admin.setPhone("+573233425899");
        admin.setPreferredNotification("EMAIL");
        admin.setBalance(50000000.0);
        admin.setRole("ADMIN");
        // Cambia la contraseña por una segura
        admin.setPassword(passwordEncoder.encode("admin123")); 

        userRepository.save(admin);
        System.out.println("Admin creado o actualizado con éxito");
    }
}