package com.example.order_service.config;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            return Collections.emptyList();
        }

        Collection<String> roles = (Collection<String>) realmAccess.get("roles");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .toList();
    }
}
