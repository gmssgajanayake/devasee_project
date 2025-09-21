package com.devasee.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // don’t checking CSRF
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                HttpMethod.GET,
                                "/api/v1/product/books",  // Spring Security treats /books and /books/ differently
                                "/api/v1/product/books/**",
                                "/api/v1/product/stationery",
                                "/api/v1/product/stationery/**",
                                "/api/v1/promo",
                                "/api/v1/promo/**",
                                "/api/v1/inventory/product/*/quantity",
                                "/api/v1/product/printing",
                                "/api/v1/product/printing/**"
                        ).permitAll() // no token required
                        .pathMatchers(
                                HttpMethod.GET,
                                "/api/v1/inventory",
                                "/api/v1/inventory/*"
                                ).hasRole("ADMIN")
                        .pathMatchers(
                                HttpMethod.POST,
                                "/api/v1/product/books",
                                "/api/v1/product/stationery",
                                "/api/v1/product/printing",
                                "/api/v1/inventory",
                                "/api/v1/promo/**",
                                "/api/v1/promo"
                        ).hasRole("ADMIN")
                        .pathMatchers(
                                HttpMethod.PUT,
                                "/api/v1/product/books",
                                "/api/v1/product/stationery",
                                "/api/v1/product/printing",
                                "api/v1/inventory",
                                "/api/v1/promo/**",
                                "/api/v1/promo"
                        ).hasRole("ADMIN")
                        .pathMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/product/books/**",
                                "/api/v1/product/stationery/**",
                                "/api/v1/product/printing/**",
                                "/api/v1/inventory/**",
                                "/api/v1/promo/**",
                                "/api/v1/promo"
                        ).hasRole("ADMIN")
                        .pathMatchers(
                                "/api/v1/users/admins/**",
                                "api/v1/users/admins",
                                "/api/v1/analytics",
                                "/api/v1/analytics/**"
                        ).hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2 // Authentication via OAuth2 Resource Server
                        .authenticationEntryPoint(jsonAuthEntryPoint) // handles 401 Unauthorized
                        .accessDeniedHandler(jsonAccessDeniedHandler) // handles 403 Forbidden
                        .jwt(jwt -> jwt
                                .jwtDecoder(clerkJwtDecoderConfig.jwtDecoder()) // custom JWT decoder
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter) // map JWT claims → Spring Security Authentication
                        )
                );

                // When a request has an Authorization: Bearer <token> header, The token is decoded by
                // clerkJwtDecoderConfig.jwtDecoder(), Claims are converted into Spring Authentication using
                // customJwtAuthenticationConverter
                    // If decoding fails → jsonAuthEntryPoint is triggered (401 Unauthorized)
                    // If decoding works but user lacks authority → jsonAccessDeniedHandler is triggered (403 Forbidden)

        return http.build();
    }
}
