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

    @Value("${clerk.issuer-uri}")
    private String clerkIssuerUri;

    @Value("${clerk.expected-audience}")
    private String expectedAudience;

    // Inject your user service here (Reactive)
    private final ReactiveUserService userService;

    public SecurityConfig(ReactiveUserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain (ServerHttpSecurity http) throws Exception{
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges  -> exchanges
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
                                "/api/v1/product/book/admin/**"
                                ).hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2-> oauth2
                        .authenticationEntryPoint(jsonAuthEntryPoint())
                        .jwt(jwt-> jwt
                                        .jwtDecoder(clerkJwtDecoder())
                                        .jwtAuthenticationConverter(customJwtAuthenticationConverter())
                                //.jwkSetUri(clerkJwkSetUri) //
                        )
                );

        return http.build();
    }

    // Clerk JWT Decoder with issuer & audience validation
    @Bean
    public ReactiveJwtDecoder clerkJwtDecoder() {
        NimbusReactiveJwtDecoder jwtDecoder = (NimbusReactiveJwtDecoder)
                ReactiveJwtDecoders.fromIssuerLocation(clerkIssuerUri);

        // Add audience & issuer validation
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(clerkIssuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new JwtClaimValidator<String>(
                "azp",
                azp-> azp != null && azp.equals(expectedAudience)
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
            return userService.findRolesByUserId(userId)
                    .map(roles -> {
                        var authorities = roles.stream()
                                .map(role -> "ROLE_" + role)
                                .map(SimpleGrantedAuthority::new)
                                .toList();
                        System.out.println("########## user roles : "+authorities);
                        return new JwtAuthenticationToken(jwt, authorities);
                    });
        };
    }



    // Custom JSON Authentication Entry Point for Unauthorized Requests
    @Bean
    public ServerAuthenticationEntryPoint jsonAuthEntryPoint(){
        return (exchange, ex) -> {

            System.err.println("####### Authentication error: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());


            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");

            String body = "{\"success\":false,\"message\":\"Unauthorized or invalid token\",\"errorCode\":\"AUTH_ERROR\"}";
            byte[] bytes = body.getBytes();
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(bytes)))
                    .onErrorResume(Mono::error);
        };
    }
}
