package com.devasee.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/// Handlers (Error Cases)
///  403 Forbidden → JsonAccessDeniedHandler writes a JSON error response
/// When an authenticated user tries to access an endpoint they’re not authorized for

@Component
public class JsonAccessDeniedHandler implements ServerAccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(JsonAccessDeniedHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {

        log.error("### Access denied error: {} - {}", denied.getClass().getSimpleName(), denied.getMessage());

        // Set Response Status & Headers
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        // Custom Json Response
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

        // Write JSON to Response Body
        byte[] bytes = body.getBytes();
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)))
                .onErrorResume(Mono::error); // If writing fails, propagate the error
    }
}
