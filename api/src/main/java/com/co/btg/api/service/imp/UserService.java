package com.co.btg.api.service.imp;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.co.btg.api.dto.RegisterUserRequest;
import com.co.btg.api.dto.UpdatePasswordRequest;
import com.co.btg.api.dto.UpdateUserRequest;
import com.co.btg.api.dto.UpdateUserRoleRequest;
import com.co.btg.api.dto.UserResponse;
import com.co.btg.api.exceptions.UserNotFoundException;
import com.co.btg.api.models.User;
import com.co.btg.api.repositories.GenericRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	 private final GenericRepository<User> userRepository;
	    private final PasswordEncoder passwordEncoder;

	    // Crear usuario nuevo (siempre con rol USER)
	    public UserResponse createUser(RegisterUserRequest dto) {
	        User user = new User();
	        user.setUserId(UUID.randomUUID().toString());
	        user.setBalance(dto.getBalance());
	        user.setEmail(dto.getEmail());
	        user.setName(dto.getName());
	        user.setPhone(dto.getPhone());
	        user.setRole("USER");
	        user.setPassword(passwordEncoder.encode(dto.getPassword())); // FIX
	        user.setPreferredNotification(dto.getPreferredNotification());
	        userRepository.save(user);
	        return toResponse(user);
	    }

	    // Actualizar datos de un usuario (no toca password ni rol)
	    public UserResponse updateUser(UpdateUserRequest dto) throws UserNotFoundException {
	        User existing = findById(dto.getUserId());
	        existing.setBalance(dto.getBalance());
	        existing.setEmail(dto.getEmail());
	        existing.setName(dto.getName());
	        existing.setPhone(dto.getPhone());
	        existing.setPreferredNotification(dto.getPreferredNotification());
	        userRepository.save(existing);
	        return toResponse(existing);
	    }

	    // Actualizar rol (solo admin)
	    public UserResponse updateUserRole(UpdateUserRoleRequest dto) throws UserNotFoundException {
	        User existing = findById(dto.getUserId());
	        existing.setRole(dto.getRole().equalsIgnoreCase("ADMIN") ? "ADMIN" : "USER");
	        userRepository.save(existing);
	        return toResponse(existing);
	    }

	    // Cambiar password
	    public void updatePassword(UpdatePasswordRequest dto) throws UserNotFoundException {
	        User existing = findById(dto.getUserId());
	        // validamos que la contraseña actual coincida
	        if (!passwordEncoder.matches(dto.getOldPassword(), existing.getPassword())) {
	            throw new IllegalArgumentException("La contraseña actual no es correcta");
	        }
	        existing.setPassword(passwordEncoder.encode(dto.getNewPassword()));
	        userRepository.save(existing);
	    }

	    // Métodos de consulta
	    public User findById(String userId) throws UserNotFoundException {
	        return userRepository.findById(userId)
	                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + userId));
	    }

	    public List<UserResponse> findAll() {
	        return userRepository.findAll().stream()
	                .map(this::toResponse)
	                .collect(Collectors.toList());
	    }

	    // Mapper a DTO seguro
	    public UserResponse toResponse(User user) {
	        return UserResponse.builder()
	                .userId(user.getUserId())
	                .name(user.getName())
	                .email(user.getEmail())
	                .phone(user.getPhone())
	                .preferredNotification(user.getPreferredNotification())
	                .balance(user.getBalance())
	                .role(user.getRole())
	                .build();
	    }
	}