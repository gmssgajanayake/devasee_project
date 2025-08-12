package com.devasee.apigateway.filter;

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

@Component
public class AddUserHeaderFilter implements GlobalFilter, Ordered {

    private static final String HEADER_NAME = "X-User-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("AddUserHeaderFilter: Incoming request to " + exchange.getRequest().getURI());
        String path = exchange.getRequest().getURI().getPath();

        // Skip adding header for public endpoints like /api/v1/users/customer/public/save
        if (path.contains("/public/")) {
            System.out.println("AddUserHeaderFilter: Skipping header for public endpoint " + path);
            return chain.filter(exchange);
        }

        // Get Authentication from ReactiveSecurityContextHolder
        return ReactiveSecurityContextHolder.getContext()// Accesses the current security context reactively
                .map(SecurityContext::getAuthentication) // Extracts the Authentication object from the security context
                .flatMap(auth -> {

                            if (auth instanceof JwtAuthenticationToken jwtAuth) {
                                String userId = jwtAuth.getToken().getClaimAsString("userId"); // Extracts the userId claim from the JWT token
                                String tokenValue = jwtAuth.getToken().getTokenValue();
                                System.out.println("AddUserHeaderFilter: Extracted userId = " + userId);

                                ServerHttpRequest.Builder mutatedRequestBuilder = exchange.getRequest().mutate();

                                if (userId != null && !userId.isEmpty()) {
                                    mutatedRequestBuilder.header(HEADER_NAME, userId);
                                    System.out.println("##### Added header " + HEADER_NAME + ": " + userId);
                                }

                                System.out.println("AddUserHeaderFilter: userId end with /save ? = " + path.endsWith("/save"));
                                String originalAuthHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

                                // For user save endpoint, also add the full Authorization header with token
                                if (path.endsWith("/save")) {
                                    System.out.println("######### 1");
                                    try{
                                        mutatedRequestBuilder.header("Authorization", originalAuthHeader);
                                        System.out.println("######### 2");
                                    } catch (Exception e){
                                        System.out.println("########### e: " + e.getMessage());
                                    }
                                }
                                System.out.println("######### 3");

                                ServerHttpRequest mutatedRequest = mutatedRequestBuilder.build();

                                ServerWebExchange mutatedExchange = exchange.mutate()
                                        .request(mutatedRequest)
                                        .build();
                                System.out.println("######### 5");

                                // Log all headers after adding custom one (simulate what will be sent)
                                System.out.println("@@@@@@@@@ Headers after adding custom header:");
                                mutatedRequestBuilder.build().getHeaders().forEach((key, values) -> {
                                    System.out.println(key + ": " + String.join(",", values));
                                });

                                return chain.filter(mutatedExchange);
                            }

                    System.out.println("AddUserHeaderFilter: userId null, passing original exchange");
                    // If userId is missing, continues the chain with the original exchange (no header added)
                    return chain.filter(exchange);
                })
                .onErrorResume(e-> {
                    System.err.println("AddUserHeaderFilter: Error extracting userId from JWT: " + e.getMessage());
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
