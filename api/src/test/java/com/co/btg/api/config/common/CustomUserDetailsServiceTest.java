package com.co.btg.api.config.common;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.co.btg.api.config.security.common.CustomUserDetailsService;
import com.co.btg.api.models.User;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

class CustomUserDetailsServiceTest {

    @Mock
    private DynamoDbTable<User> userTable;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PageIterable<User> mockPageIterable;

    @Mock
    private Page<User> mockPage;

    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsService(userTable, passwordEncoder);
    }

@Test
@SuppressWarnings("unchecked")
void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
    // Arrange
    User user = new User();
    user.setEmail("test@test.com");
    user.setPassword("encodedPassword");
    user.setRole("USER");

    // Mock PageIterable<User>
    PageIterable<User> mockPageIterable = mock(PageIterable.class);
    when(userTable.scan(any(Consumer.class))).thenReturn(mockPageIterable);

    // Truco: devolvemos un SdkIterable real usando un lambda
    SdkIterable<User> sdkIterable = () -> List.of(user).iterator();
    when(mockPageIterable.items()).thenReturn(sdkIterable);

    // Act
    UserDetails result = customUserDetailsService.loadUserByUsername("test@test.com");

    // Assert
    assertNotNull(result);
    assertEquals("test@test.com", result.getUsername());
    assertEquals("encodedPassword", result.getPassword());
    assertTrue(result.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
}



@Test
void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
    PageIterable<User> mockPageIterable = mock(PageIterable.class);
    when(userTable.scan(any(Consumer.class))).thenReturn(mockPageIterable);

    SdkIterable<User> mockItems = mock(SdkIterable.class);
    when(mockPageIterable.items()).thenReturn(mockItems);
    when(mockItems.iterator()).thenReturn(List.<User>of().iterator());

    assertThrows(UsernameNotFoundException.class,
            () -> customUserDetailsService.loadUserByUsername("notfound@test.com"));
}

}
