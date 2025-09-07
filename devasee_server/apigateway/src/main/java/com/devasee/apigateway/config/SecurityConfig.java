package com.devasee.apigateway.config;

import com.devasee.apigateway.service.ReactiveUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebFluxSecurity // This is the reactive version
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${clerk.issuer-uri}")
    private String clerkIssuerUri;

    @Value("${clerk.expected-audience-customer}")
    private String expectedAudienceCustomer;
    @Value("${clerk.expected-audience-admin}")
    private String expectedAudienceAdmin;

    // Inject your user service here (Reactive)
    private final ReactiveUserService reactiveUserService;

    public SecurityConfig(ReactiveUserService reactiveUserService) {
        this.reactiveUserService = reactiveUserService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
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
                        .authenticationEntryPoint(jsonAuthEntryPoint())
                        .accessDeniedHandler(jsonAccessDeniedHandler()) // 403
                        .jwt(jwt -> jwt
                                .jwtDecoder(clerkJwtDecoder())
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    // Custom JSON Access Denied Handler for Forbidden Requests (403)
    @Bean
    public ServerAccessDeniedHandler jsonAccessDeniedHandler() {
        return (exchange, ex) -> {
            log.error("### Access denied error: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");

            String body = """
                    {
                        "success": false,
                        "message": "Access denied. You don't have permission to access this resource.",
                        "data": {
                                "status": 403,
                                "requiredRole": "ADMIN"
                            }
                    }
                    """;

            byte[] bytes = body.getBytes();
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(bytes)))
                    .onErrorResume(Mono::error);
        };
    }

    // Clerk JWT Decoder with issuer & audience validation
    @Bean
    public ReactiveJwtDecoder clerkJwtDecoder() {
        NimbusReactiveJwtDecoder jwtDecoder = ReactiveJwtDecoders.fromIssuerLocation(clerkIssuerUri);

        // Add audience & issuer validation
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(clerkIssuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new JwtClaimValidator<String>(
                "azp",
                azp -> azp != null &&
                        (azp.equals(expectedAudienceCustomer) || azp.equals(expectedAudienceAdmin))
        );

        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience)); // (withIssuer,withAudience)

        return jwtDecoder;
    }

    // Custom converter that loads roles from your user service
    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> customJwtAuthenticationConverter() {
        return jwt -> {

            String userId = jwt.getClaimAsString("userId"); // or "userId", whichever you use

            // Call your user service here asynchronously to get roles for the user
            // For example, assuming you have a ReactiveUserService returning Mono<List<String>>
            return reactiveUserService.findRolesByUserId(userId)
                    .onErrorResume(e -> {
                        log.error("### Error fetching roles, using empty list: {}", e.getMessage());
                        return Mono.just(Collections.emptyList());
                    })
                    .map(roles -> {
                        var authorities = roles.stream()
                                .map(role -> "ROLE_" + role)
                                .map(SimpleGrantedAuthority::new)
                                .toList();

                        log.info("### User roles: {}", authorities);
                        return new JwtAuthenticationToken(jwt, authorities);
                    });

        };
    }

    // Custom JSON Authentication Entry Point for Unauthorized Requests
    @Bean
    public ServerAuthenticationEntryPoint jsonAuthEntryPoint(){
        return (exchange, ex) -> {

            log.error("### Authentication error: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");

            String body = """
                    {"success": false,
                    "message": "%s",
                    "data": "AUTH_ERROR"
                    }""".formatted(ex.getMessage());

            byte[] bytes = body.getBytes();
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(bytes)))
                    .onErrorResume(Mono::error);
        };
    }
}
