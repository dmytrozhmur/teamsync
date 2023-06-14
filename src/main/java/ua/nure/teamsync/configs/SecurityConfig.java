package ua.nure.teamsync.configs;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import ua.nure.teamsync.converters.JwtRoleAndScopeConverter;

import java.net.MalformedURLException;
import java.net.URI;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtRoleAndScopeConverter jwtRoleConverter;
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwtRoleConverter);

        return http
                .authorizeRequests(authz -> authz
                        .antMatchers(HttpMethod.POST, "/api/v1/teams")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('write')")
                        .antMatchers(HttpMethod.PUT, "/api/v1/teams/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('write')")
                        .antMatchers(HttpMethod.PATCH, "/api/v1/teams/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('write')")
                        .antMatchers(HttpMethod.DELETE, "/api/v1/teams/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('delete')")
                        .antMatchers(HttpMethod.GET, "/api/v1/teams")
                        .access("hasAnyRole('ADMIN', 'PERFORMER') AND hasAuthority('retrieve')")
                        .antMatchers(HttpMethod.GET, "/api/v1/teams/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER', 'PERFORMER') AND hasAuthority('retrieve')")
                        .antMatchers(HttpMethod.POST, "/api/v1/team_members")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('write')")
                        .antMatchers(HttpMethod.PUT, "/api/v1/team_members/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('write')")
                        .antMatchers(HttpMethod.PATCH, "/api/v1/team_members/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('write')")
                        .antMatchers(HttpMethod.DELETE, "/api/v1/team_members/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('delete')")
                        .antMatchers(HttpMethod.GET, "/api/v1/team_members")
                        .access("hasRole('ADMIN') AND hasAuthority('retrieve')")
                        .antMatchers(HttpMethod.GET, "/api/v1/team_members/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER', 'PERFORMER') AND hasAuthority('retrieve')")
                        .antMatchers(HttpMethod.POST, "/api/v1/team_members/**/conditions")
                        .permitAll()
                        .antMatchers(HttpMethod.POST, "/api/v1/tasks")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('write')")
                        .antMatchers(HttpMethod.PUT, "/api/v1/tasks/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('write')")
                        .antMatchers(HttpMethod.PATCH, "/api/v1/tasks/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER', 'PERFORMER') AND hasAuthority('write')")
                        .antMatchers(HttpMethod.DELETE, "/api/v1/tasks/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER') AND hasAuthority('delete')")
                        .antMatchers(HttpMethod.GET, "/api/v1/tasks")
                        .access("hasAnyRole('ADMIN') AND hasAuthority('retrieve')")
                        .antMatchers(HttpMethod.GET, "/api/v1/tasks/**")
                        .access("hasAnyRole('ADMIN', 'MANAGER', 'PERFORMER') AND hasAuthority('retrieve')")
                        .antMatchers(HttpMethod.GET, "/api/v1/payment/client_token")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .antMatchers(HttpMethod.POST, "/api/v1/payment/checkout")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .antMatchers(HttpMethod.POST, "/api/v1/sync")
                        .access("hasAuthority('write') AND hasAuthority('delete')")
                        .anyRequest()
                        .authenticated())
                //.authorizeHttpRequests(authz -> authz.antMatchers("").acc.anyRequest().authenticated())
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(authenticationConverter)
                .and()
                .and()
                .build();
    }

//    @Bean
//    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
//        RSAKey rsaKey = generateRsa();
//        JWKSet jwkSet = new JWKSet(rsaKey);
//        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
//    }
//
    @Bean
    public JwtDecoder jwtDecoder() {
        final JWKSource<SecurityContext> jwkSource;
        try {
            jwkSource = new RemoteJWKSet<>(URI.create(jwkSetUri).toURL());
            ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
            jwtProcessor.setJWSKeySelector(new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource));
            jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {});
            return new NimbusJwtDecoder(jwtProcessor);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return null;
        }

       // SecurityContext securityContext = new SimpleSecurityContext();
        //jwtProcessor.(securityContext);
    }
}
