package com.devasee.delivery.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        log.error("### Access denied error: {} - {}", accessDeniedException.getClass().getSimpleName(), accessDeniedException.getMessage());

        // Set Response Status & Headers
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON Response
        String json = """
                {
                    "success": false,
                    "message": "Access denied. You don't have permission to access this resource.",
                    "data":{
                        "status": 403,
                        "requiredRole": "ADMIN"
                    }
                }
                """;

        // Write and flush response
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
