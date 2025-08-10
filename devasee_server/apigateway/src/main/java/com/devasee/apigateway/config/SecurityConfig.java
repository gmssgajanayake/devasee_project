package com.devasee.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

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
