package com.devasee.apigateway.config;

import com.devasee.apigateway.service.ReactiveUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter; // convert JWT into Spring Security auth tokens
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity // This is the reactive version
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private  String clerkJwkSetUri ;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain (ServerHttpSecurity http) throws Exception{
        http
                .authorizeExchange(exchanges  -> exchanges
                        .pathMatchers("/api/v1/promo/public/**").permitAll()
                        .pathMatchers("/api/v1/product/book/public/**").permitAll()
                        .pathMatchers("/api/v1/product/stationery/public/**").permitAll()
                        .pathMatchers("/api/v1/product/printing/public/**").permitAll()
                        .pathMatchers("/api/v1/inventory/public/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2-> oauth2
                        .jwt(jwt-> jwt.jwkSetUri(clerkJwkSetUri))
                );

        return http.build();
    }
}
