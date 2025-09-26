package com.devasee.apigateway.config;

import com.devasee.apigateway.service.ReactiveUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/// The bridge between a valid JWT and Spring Security’s authentication object with roles
/// After the JWT passes signature, issuer, and audience checks (via ClerkJwtDecoderConfig
/// This converter decides which roles/authorities the user has, based on our data

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private static final Logger log = LoggerFactory.getLogger(CustomJwtAuthenticationConverter.class);

    private final ReactiveUserService reactiveUserService;

    public CustomJwtAuthenticationConverter(ReactiveUserService reactiveUserService) {
        this.reactiveUserService = reactiveUserService;
    }

    // Once the token is verified, Spring passes the decoded Jwt here
    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {

        // Extract User Identifier issued by clerk token's claims
        String userId = jwt.getClaimAsString("userId"); // or "userId", whichever you use

        log.info("### Option selection area");

        // ---- CASE 1: New user creation endpoint ----
        // Check if current request is POST /api/v1/users
        return Mono.deferContextual(ctx -> {
            ServerWebExchange exchange = ctx.get(ServerWebExchange.class);
            String path = exchange.getRequest().getURI().getPath();
            HttpMethod method = exchange.getRequest().getMethod();

            if ("/api/v1/users/auth".equals(path) && HttpMethod.POST.equals(method)) {
                log.info("### User creation endpoint detected, assigning default ROLE_CUSTOMER");
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
                return Mono.just(new JwtAuthenticationToken(jwt, authorities));
            }


            // ---- CASE 2: All other requests ----

            // Call your user service here asynchronously to get roles for the user
            // For example, assuming you have a ReactiveUserService returning Mono<List<String>>
            // Let fetch user roles from user microservice using userId in clerk token
            // creates a JwtAuthenticationToken → which Spring Security uses for authorization checks (hasRole("ADMIN"),...)

            return reactiveUserService.findRoleAccountStatusByUserId(userId)
                    .onErrorResume(e -> {
                        log.error("### Error fetching roles, using empty list: {}", e.getMessage()); // When DB/service call fails
                        return Mono.empty(); // no Authentication → triggers 401
                    })
                    .flatMap(userData -> {
                        if (!userData.isUserActive()) {
                            log.warn("### User {} is inactive, denying access", userId);
                            return Mono.empty(); // no Authentication → triggers 401
                        }

                        var authorities = userData.getRoles().stream()
                                .map(role -> "ROLE_" + role)
                                .map(SimpleGrantedAuthority::new) // Converts roles into Spring Security’s format("ADMIN" -> ROLE_ADMIN)
                                .toList();

                        // Build Authentication Token, WHY?
                        // Spring Security doesn’t directly use the raw JWT string to make decisions
                        // Instead, it works with its own object model (Authentication interface)
                        // Spring’s internal representation of “this user is authenticated with these authorities.”
                        // The JWT → is kept inside it (we can still access claims)
                        log.info("### User roles: {}", authorities);
                        return Mono.just(new JwtAuthenticationToken(jwt, authorities));
                    });
        });
    }
}
