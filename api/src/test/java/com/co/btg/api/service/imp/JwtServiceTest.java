package com.co.btg.api.service.imp;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;

class JwtServiceTest {

    private JwtService jwtService;
    private static final String SECRET_KEY = "testSecretKeyWithMinimum256BitsForHS256Algorithm";
    private static final long EXPIRATION_MINUTES = 30L;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", EXPIRATION_MINUTES);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // Arrange
        String username = "testUser";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        // Act
        String token = jwtService.generateToken(username, roles);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        
        // Verify token contents
        String extractedUsername = jwtService.validateAndGetUsername(token);
        List<String> extractedRoles = jwtService.getRolesFromToken(token);
        
        assertEquals(username, extractedUsername);
        assertEquals(roles, extractedRoles);
    }

    @Test
    void validateAndGetUsername_WithValidToken_ShouldReturnUsername() {
        // Arrange
        String username = "testUser";
        String token = jwtService.generateToken(username, List.of("ROLE_USER"));

        // Act
        String extractedUsername = jwtService.validateAndGetUsername(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateAndGetUsername_WithInvalidToken_ShouldReturnNull() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        String result = jwtService.validateAndGetUsername(invalidToken);

        // Assert
        assertNull(result);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnClaims() {
        // Arrange
        String username = "testUser";
        List<String> roles = Arrays.asList("ROLE_USER");
        String token = jwtService.generateToken(username, roles);

        // Act
        Claims claims = jwtService.validateToken(token);

        // Assert
        assertNotNull(claims);
        assertEquals(username, claims.getSubject());
        assertNotNull(claims.getExpiration());
        assertNotNull(claims.getIssuedAt());
    }

    @Test
    void validateToken_WithInvalidToken_ShouldThrowException() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(Exception.class, () -> 
            jwtService.validateToken(invalidToken)
        );
    }

    @Test
    void getRolesFromToken_WithValidToken_ShouldReturnRoles() {
        // Arrange
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        String token = jwtService.generateToken("testUser", roles);

        // Act
        List<String> extractedRoles = jwtService.getRolesFromToken(token);

        // Assert
        assertEquals(roles, extractedRoles);
    }

    @Test
    void getRolesFromToken_WithInvalidToken_ShouldReturnEmptyList() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        List<String> roles = jwtService.getRolesFromToken(invalidToken);

        // Assert
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }
}
