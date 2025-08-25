package com.co.btg.api.service.imp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.co.btg.api.dto.RegisterUserRequest;
import com.co.btg.api.dto.UpdatePasswordRequest;
import com.co.btg.api.dto.UpdateUserRequest;
import com.co.btg.api.dto.UpdateUserRoleRequest;
import com.co.btg.api.dto.UserResponse;
import com.co.btg.api.exceptions.UserNotFoundException;
import com.co.btg.api.models.User;
import com.co.btg.api.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldCreateUserSuccessfully() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPhone("+1234567890");
        request.setPassword("password123");
        request.setBalance(1000.0);
        request.setPreferredNotification("EMAIL");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        UserResponse response = userService.createUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getPhone(), response.getPhone());
        assertEquals(request.getBalance(), response.getBalance());
        assertEquals("USER", response.getRole());
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WithValidData_ShouldUpdateSuccessfully() {
        // Arrange
        String userId = "user123";
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUserId(userId);
        request.setName("Updated Name");
        request.setEmail("updated@example.com");
        request.setPhone("+0987654321");
        request.setBalance(2000.0);
        request.setPreferredNotification("SMS");

        User existingUser = new User();
        existingUser.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        UserResponse response = userService.updateUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getPhone(), response.getPhone());
        assertEquals(request.getBalance(), response.getBalance());
        assertEquals(request.getPreferredNotification(), response.getPreferredNotification());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserRole_ToAdmin_ShouldUpdateRoleSuccessfully() {
        // Arrange
        String userId = "user123";
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        request.setUserId(userId);
        request.setRole("ADMIN");

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setRole("USER");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        UserResponse response = userService.updateUserRole(request);

        // Assert
        assertNotNull(response);
        assertEquals("ADMIN", response.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updatePassword_WithCorrectOldPassword_ShouldUpdateSuccessfully() {
        // Arrange
        String userId = "user123";
        String oldPassword = "oldPass123";
        String newPassword = "newPass123";
        
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setUserId(userId);
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setPassword("encodedOldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(oldPassword, existingUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Act & Assert
        assertDoesNotThrow(() -> userService.updatePassword(request));
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(newPassword);
    }

    @Test
    void updatePassword_WithIncorrectOldPassword_ShouldThrowException() {
        // Arrange
        String userId = "user123";
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setUserId(userId);
        request.setOldPassword("wrongPass");
        request.setNewPassword("newPass123");

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setPassword("encodedOldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(request.getOldPassword(), existingUser.getPassword())).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> userService.updatePassword(request));
        assertEquals("La contraseña actual no es correcta", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findById_WithExistingUser_ShouldReturnUser() {
        // Arrange
        String userId = "user123";
        User user = new User();
        user.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    void findById_WithNonExistingUser_ShouldThrowException() {
        // Arrange
        String userId = "nonexistent";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
            () -> userService.findById(userId));
        assertEquals("Usuario no encontrado con id: " + userId, exception.getMessage());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(
            createTestUser("user1", "User One"),
            createTestUser("user2", "User Two")
        );
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.findAll();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("User One", responses.get(0).getName());
        assertEquals("User Two", responses.get(1).getName());
    }

    private User createTestUser(String id, String name) {
        User user = new User();
        user.setUserId(id);
        user.setName(name);
        user.setEmail(name.toLowerCase().replace(" ", ".") + "@example.com");
        return user;
    }
}
