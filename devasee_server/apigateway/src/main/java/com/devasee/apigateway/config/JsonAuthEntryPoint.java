package com.devasee.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

///  Handlers (Error Cases)
/// 401 Unauthorized → JsonAuthEntryPoint writes a JSON error response.

@Component // auto-detect and register the class as a bean in the application context.
public class JsonAuthEntryPoint implements ServerAuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(JsonAuthEntryPoint.class);

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

            log.error("### Authentication error: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // Set Response Status & Headers
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");

            // Custom Json Response
            String body = """
                    {"success": false,
                    "message": "%s",
                    "data": "AUTH_ERROR"
                    }""".formatted(ex.getMessage());

            // Write JSON to Response Body
            byte[] bytes = body.getBytes();
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(bytes)))
                    .onErrorResume(Mono::error); // If writing fails, propagate the error
    }
}
