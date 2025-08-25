package com.co.btg.api.config.common;


import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import com.co.btg.api.config.security.common.JwtAuthConverter;

class JwtAuthConverterTest {

    private JwtAuthConverter converter;

    @BeforeEach
    void setUp() {
        converter = new JwtAuthConverter("cognito:groups");
    }

  private Jwt buildJwt(Map<String, Object> claims) {
    Map<String, Object> safeClaims = new HashMap<>();
    safeClaims.put("sub", "test-user"); // claim mínimo requerido
    safeClaims.putAll(claims);

    return new Jwt(
            "tokenValue",
            Instant.now(),
            Instant.now().plusSeconds(3600),
            Map.of("alg", "none"),
            safeClaims
    );
}

    @Test
    void convert_shouldReturnEmptyAuthorities_whenNoClaims() {
    Jwt jwt = buildJwt(Map.of()); // claims vacío -> se completa con "sub"

    Collection<GrantedAuthority> authorities = converter.convert(jwt);

    assertNotNull(authorities);
    assertTrue(authorities.isEmpty(), "No debe retornar authorities si no hay scope ni groups");
}

    @Test
    void convert_shouldReturnAuthoritiesFromScopeClaim() {
        Jwt jwt = buildJwt(Map.of("scope", "read write"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        Set<String> authStrings = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        assertEquals(Set.of("read", "write"), authStrings);
    }

    @Test
    void convert_shouldReturnAuthoritiesFromGroupsClaim() {
        Jwt jwt = buildJwt(Map.of("cognito:groups", List.of("admin", "user")));

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        Set<String> authStrings = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        assertEquals(Set.of("ROLE_ADMIN", "ROLE_USER"), authStrings);
    }

    @Test
    void convert_shouldMergeAuthoritiesFromScopeAndGroups() {
        Jwt jwt = buildJwt(Map.of(
                "scope", "read",
                "cognito:groups", List.of("admin")
        ));

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        Set<String> authStrings = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        assertEquals(Set.of("read", "ROLE_ADMIN"), authStrings);
    }

    @Test
    void convert_shouldHandleNullGroupsGracefully() {
        Jwt jwt = buildJwt(Map.of("scope", "read"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        Set<String> authStrings = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        assertEquals(Set.of("read"), authStrings);
    }
}

