package com.co.btg.api.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.co.btg.api.dto.RegisterUserRequest;
import com.co.btg.api.dto.UpdatePasswordRequest;
import com.co.btg.api.dto.UpdateUserRequest;
import com.co.btg.api.dto.UpdateUserRoleRequest;
import com.co.btg.api.dto.UserResponse;
import com.co.btg.api.models.User;
import com.co.btg.api.service.imp.UserService;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUser_ShouldReturnUser() {
        // Arrange
        String userId = "user123";
        User user = new User();
        UserResponse userResponse = new UserResponse();
        when(userService.findById(userId)).thenReturn(user);
        when(userService.toResponse(user)).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> response = userController.getUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(userService).findById(userId);
        verify(userService).toResponse(user);
    }

    @Test
    void createUser_ShouldCreateAndReturnUser() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest();
        UserResponse expectedResponse = new UserResponse();
        when(userService.createUser(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserResponse> response = userController.createUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        assertEquals(500000.0, request.getBalance());
        verify(userService).createUser(request);
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser() {
        // Arrange
        UpdateUserRequest request = new UpdateUserRequest();
        UserResponse expectedResponse = new UserResponse();
        when(userService.updateUser(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserResponse> response = userController.updateUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(userService).updateUser(request);
    }

    @Test
    void updateUserRole_ShouldUpdateAndReturnUser() {
        // Arrange
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        UserResponse expectedResponse = new UserResponse();
        when(userService.updateUserRole(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserResponse> response = userController.updateUserRole(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(userService).updateUserRole(request);
    }

    @Test
    void updatePassword_ShouldUpdatePasswordSuccessfully() {
        // Arrange
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        doNothing().when(userService).updatePassword(request);

        // Act
        ResponseEntity<?> response = userController.updatePassword(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contraseña actualizada correctamente", response.getBody());
        verify(userService).updatePassword(request);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        List<UserResponse> expectedUsers = Arrays.asList(
            new UserResponse(),
            new UserResponse()
        );
        when(userService.findAll()).thenReturn(expectedUsers);

        // Act
        ResponseEntity<List<UserResponse>> response = userController.getAllUser();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
        verify(userService).findAll();
    }
}
