package com.co.btg.api.config.local;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co.btg.api.config.security.common.JwtAuthenticationFilter;
import com.co.btg.api.config.security.local.SecurityConfigLocal;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Import(SecurityConfigLocal.class)
class SecurityConfigLocalTest {

    @RestController
class DummyAuthController {
    @GetMapping("/auth/login")
    public String login() {
        return "OK";
    }
}

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void passwordEncoder_shouldEncodeAndMatch() {
        String rawPassword = "12345";
        String encoded = passwordEncoder.encode(rawPassword);

        assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
    }

    @Test
    void jwtDecoder_shouldBeCreated() {
        assertThat(jwtDecoder).isNotNull();
    }

    @Test
    void jwtEncoder_shouldThrowIfSecretTooShort() {
        JwtAuthenticationFilter mockFilter = mock(JwtAuthenticationFilter.class);
        SecurityConfigLocal config = new SecurityConfigLocal(mockFilter);

        String shortSecret = "short-secret";

        assertThatThrownBy(() -> config.jwtEncoder(shortSecret))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("al menos 32 caracteres");
    }

    @Test
    void jwtAuthConverterLocal_shouldAssignDefaultRole() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "HS256")
                .claim("sub", "user")
                .build();

        var authorities = jwtAuthenticationConverter.convert(jwt).getAuthorities();

        assertThat(authorities).extracting("authority")
                .containsExactly("ROLE_USER");
    }

   @Test
void filterChain_shouldPermitAuthEndpoints() throws Exception {
    mockMvc.perform(get("/auth/login"))
            .andExpect(result -> {
                int status = result.getResponse().getStatus();
                assertThat(status).isNotIn(401, 403); // no bloqueado por seguridad
            });
}

    @Test
    void filterChain_shouldDenyProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/protected"))
                .andExpect(status().isForbidden()); // requiere autenticación
    }

}

