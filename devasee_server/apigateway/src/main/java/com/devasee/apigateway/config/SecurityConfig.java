package com.devasee.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // This is the reactive v
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JsonAuthEntryPoint jsonAuthEntryPoint,
            JsonAccessDeniedHandler jsonAccessDeniedHandler,
            ClerkJwtDecoderConfig clerkJwtDecoderConfig,
            CustomJwtAuthenticationConverter customJwtAuthenticationConverter
    ) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/api/v1/promo/public/**",
                                "/api/v1/product/book/public/**",
                                "/api/v1/product/stationery/public/**",
                                "/api/v1/product/printing/public/**",
                                "/api/v1/inventory/public/**"
                        ).permitAll()
                        .pathMatchers(
                                "/api/v1/analytics/admin/**",
                                "/api/v1/inventory/admin/**",
                                "/api/v1/product/book/admin/**",
                                "/api/v1/inventory/admin/**",
                                "/api/v1/users/admin/**"
                        ).hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(jsonAuthEntryPoint)
                        .accessDeniedHandler(jsonAccessDeniedHandler) // 403
                        .jwt(jwt -> jwt
                                .jwtDecoder(clerkJwtDecoderConfig.jwtDecoder())
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter)
                        )
                );

        return http.build();
    }
}
