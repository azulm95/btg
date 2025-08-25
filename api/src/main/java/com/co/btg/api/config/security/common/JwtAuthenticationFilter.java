package com.co.btg.api.config.security.common;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.co.btg.api.service.imp.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
            	
            	Claims claims = jwtService.validateToken(token);
            	String username = claims.getSubject();
            	List<String> roles = claims.get("roles", List.class);

            	List<SimpleGrantedAuthority> authorities = roles.stream()
            	        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            	        .toList();

            	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            	        username,
            	        null,
            	        authorities
            	);
            	SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                // Token inválido, no hacemos nada y la request será rechazada por Spring Security
            }
        }

        filterChain.doFilter(request, response);
    }
}

