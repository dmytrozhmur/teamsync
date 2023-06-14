package ua.nure.teamsync.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRoleAndScopeConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        @SuppressWarnings("unchecked")
        List<String> roles = (ArrayList<String>) jwt.getClaims().get("roles");
        @SuppressWarnings("unchecked")
        List<String> authorities = (ArrayList<String>) jwt.getClaims().get("scope");

        if (roles != null && !roles.isEmpty()) {
            Collections.addAll(authorities, roles.toArray(String[]::new));
        }

        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
