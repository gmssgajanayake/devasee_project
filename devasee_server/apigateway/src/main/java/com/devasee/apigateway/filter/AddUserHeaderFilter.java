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
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String INTERNAL_JWT_HEADER = "X-Internal-JWT";

    private final InternalJWTService internalJWTService; // inject your internal JWT generator

    public AddUserHeaderFilter(InternalJWTService internalJWTService) {
        this.internalJWTService = internalJWTService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("### Incoming request getURI {}", exchange.getRequest().getURI());
        log.info("### Incoming request getMethod {}", exchange.getRequest().getMethod());
        log.info("### Incoming request path {}", path);

        // Skip adding header for public endpoints like
        if (
            "GET".equalsIgnoreCase(exchange.getRequest().getMethod().toString()) &&
            (path.startsWith("/api/v1/product") ||
            path.startsWith("/api/v1/promo") ||
            path.matches("/api/v1/product/[^/]+/quantity"))
        ) {
            log.info("### Skipping header adding for public endpoint {}", path);
            return chain.filter(exchange);
        }

        // Get Authentication from ReactiveSecurityContextHolder(Get Authenticated User from Security Context)
        // Accesses the current Authentication object reactively
        // At this point, the authentication token is already validated (JwtAuthenticationToken)
        return ReactiveSecurityContextHolder.getContext()// Accesses the current security context reactively
                .map(SecurityContext::getAuthentication) // Extracts the Authentication object from the security context
                .flatMap(auth -> {

                            if (auth instanceof JwtAuthenticationToken jwtAuthToken) {
                                // Extract userId and Roles
                                String userId = jwtAuthToken.getToken().getClaimAsString("userId"); // Extracts the userId claim from the JWT token
                                String originalAuthHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
                                log.info("### Extracted userId = {}", userId);

                                // Converts Spring authorities to simple role strings for internal JWT.
                                List<String> roles = jwtAuthToken.getAuthorities().stream()
                                        .map(a -> a.getAuthority().replace("ROLE_", ""))
                                        .toList();

                                // Generate internal JWT
                                // Downstream services don’t need to know about Clerk, they just verify the internal token
                                String internalJwt = internalJWTService.generateInternalToken(userId, roles);

                                // In Spring Cloud Gateway, ServerWebExchange is the reactive HTTP request/response
                                // context. It’s immutable, meaning you cannot modify the original request or response directly
                                // if we want to add headers, change the path, or modify anything, you create a new
                                // ServerWebExchange based on the original one.
                                // This avoids race conditions and allows multiple requests to be processed concurrently
                                // on the same thread.
                                ServerHttpRequest.Builder mutatedRequestBuilder = exchange.getRequest().mutate();

                                // Adds X-User-Id and X-Internal-JWT headers.
                                if (userId != null && !userId.isEmpty()) {
                                    mutatedRequestBuilder.header(USER_ID_HEADER, userId);
                                    log.info("### Added header {}: {}", USER_ID_HEADER, userId);
                                }

                                // Add internal JWT header
                                mutatedRequestBuilder.header(INTERNAL_JWT_HEADER, internalJwt);
                                log.info("### Added internal JWT header {}: {}", INTERNAL_JWT_HEADER, internalJwt);

                                // For user save endpoint, also add the full Authorization header with token
                                // Keeps the original Authorization only for the user save/login endpoint
                                if ("POST".equalsIgnoreCase(exchange.getRequest().getMethod().toString()) &&
                                        path.equals("/api/v1/users/auth") && originalAuthHeader != null) {
                                    mutatedRequestBuilder.header("Authorization", originalAuthHeader);
                                    log.info("### Keep Original Authorization header for user create endpoint");
                                } else {
                                    // Remove Authorization header for all other endpoints, Other downstream services rely only on internal JWT
                                    mutatedRequestBuilder.headers(headers -> headers.remove("Authorization"));
                                    log.info("### Removed original Authorization header for non-user-save endpoints");
                                }

                                // forwards the mutated request to the appropriate microservice registered in Eureka.
                                // erverHttpRequest.Builder + mutatedExchange is a reactive pattern to safely modify
                                // request/headers in non-blocking, shared-thread environments.
                                ServerHttpRequest mutatedRequest = mutatedRequestBuilder.build();
                                ServerWebExchange mutatedExchange = exchange.mutate()
                                        .request(mutatedRequest)
                                        .build();

                                // Log all headers after adding custom one (simulate what will be sent)
                                mutatedRequestBuilder.build().getHeaders().forEach((key, values)
                                        -> log.info("### Header: {} = {}", key, String.join(",", values)));

                                // Spring Cloud Gateway filters pass the ServerWebExchange down the chain (chain.filter(exchange)).
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
