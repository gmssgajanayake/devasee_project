package com.devasee.apigateway.filter;

import com.devasee.apigateway.service.InternalJWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AddUserHeaderFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AddUserHeaderFilter.class);
    private static final String HEADER_NAME = "X-User-Id";
    private static final String INTERNAL_JWT_HEADER = "X-Internal-JWT";

    private final InternalJWTService internalJWTService; // inject your internal JWT generator

    public AddUserHeaderFilter(InternalJWTService internalJWTService) {
        this.internalJWTService = internalJWTService;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("### Incoming request to {}", exchange.getRequest().getURI());

        // Skip adding header for public endpoints like /api/v1/users/customer/public/save
        if (path.contains("/public/")) {
            log.info("### Skipping header for public endpoint {}", path);
            return chain.filter(exchange);
        }

        // Get Authentication from ReactiveSecurityContextHolder
        return ReactiveSecurityContextHolder.getContext()// Accesses the current security context reactively
                .map(SecurityContext::getAuthentication) // Extracts the Authentication object from the security context
                .flatMap(auth -> {

                            if (auth instanceof JwtAuthenticationToken jwtAuth) {
                                String userId = jwtAuth.getToken().getClaimAsString("userId"); // Extracts the userId claim from the JWT token
                                String originalAuthHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
                                log.info("### Extracted userId = {}", userId);

                                List<String> roles = jwtAuth.getAuthorities().stream()
                                        .map(a -> a.getAuthority().replace("ROLE_", ""))
                                        .toList();

                                // Generate internal JWT
                                String internalJwt = internalJWTService.generateInternalToken(userId, roles);

                                ServerHttpRequest.Builder mutatedRequestBuilder = exchange.getRequest().mutate();

                                if (userId != null && !userId.isEmpty()) {
                                    mutatedRequestBuilder.header(HEADER_NAME, userId);
                                    log.info("### Added header {}: {}", HEADER_NAME, userId);
                                }

                                // Add internal JWT header
                                mutatedRequestBuilder.header(INTERNAL_JWT_HEADER, internalJwt);
                                log.info("### Added internal JWT header {}: {}", INTERNAL_JWT_HEADER, internalJwt);

                                // For user save endpoint, also add the full Authorization header with token
                                if (path.endsWith("/save") && originalAuthHeader != null) {
                                    mutatedRequestBuilder.header("Authorization", originalAuthHeader);
                                    log.info("### Added Authorization header for /save endpoint");
                                }

                                ServerHttpRequest mutatedRequest = mutatedRequestBuilder.build();
                                ServerWebExchange mutatedExchange = exchange.mutate()
                                        .request(mutatedRequest)
                                        .build();

                                // Log all headers after adding custom one (simulate what will be sent)
                                mutatedRequestBuilder.build().getHeaders().forEach((key, values)
                                        -> log.info("### Header: {} = {}", key, String.join(",", values)));

                                return chain.filter(mutatedExchange);
                            }

                    log.info("### userId not present, passing original exchange");
                    // If userId is missing, continues the chain with the original exchange (no header added)
                    return chain.filter(exchange);
                })
                .onErrorResume(e-> {
                    log.error("### Error extracting userId from JWT: {}", e.getMessage());
                    // Continue without adding the header if error occurs
                    return chain.filter(exchange);
                });
    }

    // Specifies the order of this filter execution.
    // Negative number means it runs early but after most security filters.
    @Override
    public int getOrder() {
        // Should run after security filters
        return -1;
    }
}
