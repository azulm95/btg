package com.co.btg.api.config.security.local;

import com.co.btg.api.config.security.common.JwtAuthenticationFilter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class SecurityConfigLocal {

	@Autowired
	 private final JwtAuthenticationFilter jwtAuthenticationFilter;

	    @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(requests -> requests
                            .requestMatchers("/auth/**").permitAll()
                            .anyRequest().authenticated())
                    .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder pe) {
    UserDetails admin = User.withUsername("admin")
        .password(pe.encode("admin123"))
        .roles("ADMIN")
        .build();
    UserDetails user = User.withUsername("user")
        .password(pe.encode("user123"))
        .roles("USER")
        .build();
    return new InMemoryUserDetailsManager(admin, user);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Decodifica JWT con el mismo secreto
  @Bean
  public JwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secret) {
    SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(key).build();
  }

  @Bean
  public JwtEncoder jwtEncoder(@Value("${security.jwt.secret}") String secret) {
      if (secret == null || secret.length() < 32) {
          throw new IllegalArgumentException("JWT secret debe tener al menos 32 caracteres");
      }
      SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      return new NimbusJwtEncoder(new ImmutableSecret<>(key));
  }

   
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }
  
  @Bean
  public JwtAuthenticationConverter jwtAuthConverterLocal() {
      JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
      converter.setJwtGrantedAuthoritiesConverter(jwt -> {
          // Por ejemplo, roles en claim "roles"
          List<String> roles = jwt.getClaimAsStringList("roles");
          if (roles == null) roles = List.of();
          return roles.stream()
                  .map(r -> (GrantedAuthority) () -> "ROLE_" + r)
                  .collect(Collectors.toList());
      });
      return converter;
  }

}
