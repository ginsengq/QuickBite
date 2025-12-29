package com.example.reviews_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")
    private String jwkSetUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthConverter = jwtAuthenticationConverter();

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("GET", "/api/reviews/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new Converter<Jwt, Collection<GrantedAuthority>>() {
            @Override
            public Collection<GrantedAuthority> convert(Jwt jwt) {
                Collection<GrantedAuthority> authorities = new ArrayList<>();

                // realm roles
                Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
                if (realmAccess != null && realmAccess.containsKey("roles")) {
                    Object rolesObj = realmAccess.get("roles");
                    if (rolesObj instanceof List) {
                        List<?> roles = (List<?>) rolesObj;
                        for (Object r : roles) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + r.toString()));
                        }
                    }
                }

                // also map client roles if present under resource_access
                Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
                if (resourceAccess != null) {
                    for (Object entryObj : resourceAccess.values()) {
                        if (entryObj instanceof Map) {
                            Map<?, ?> m = (Map<?, ?>) entryObj;
                            Object rolesObj = m.get("roles");
                            if (rolesObj instanceof List) {
                                for (Object r : (List<?>) rolesObj) {
                                    authorities.add(new SimpleGrantedAuthority("ROLE_" + r.toString()));
                                }
                            }
                        }
                    }
                }

                return authorities;
            }
        });
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        if (jwkSetUri == null || jwkSetUri.isEmpty()) return null;
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
