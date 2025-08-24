package com.co.btg.api.config.security.common;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

public class JwtAuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String groupsClaimName;

    public JwtAuthConverter(String groupsClaimName) {
        this.groupsClaimName = groupsClaimName;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // 1) Authorities por "scope" (local JWT)
        Optional.ofNullable(jwt.getClaimAsString("scope"))
                .stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .map(s -> (GrantedAuthority) () -> s)
                .forEach(authorities::add);

        // 2) Groups de Cognito
        List<String> groups = Optional.ofNullable(jwt.getClaimAsStringList(groupsClaimName))
                                      .orElse(List.of());
        groups.stream()
              .map(g -> (GrantedAuthority) () -> "ROLE_" + g.toUpperCase())
              .forEach(authorities::add);

        return authorities;
    }
}