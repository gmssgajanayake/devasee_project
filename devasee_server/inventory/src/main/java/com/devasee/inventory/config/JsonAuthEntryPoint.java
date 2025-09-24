package com.devasee.inventory.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(JsonAuthEntryPoint.class);

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        // Log the authentication error
        log.error("### Authentication error: {} - {}",
                authException.getClass().getSimpleName(),
                authException.getMessage());

        // Set response content type and status
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Build JSON response
        String json = """
                {
                    "success": false,
                    "message": "%s",
                    "data": "INTERNAL_AUTH_ERROR"
                }
                """.formatted(authException.getMessage());

        // Write JSON to response body
        response.getWriter().write(json);
    }
}
