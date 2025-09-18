package com.devasee.orders.filter;

import com.devasee.orders.services.InternalJWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InternalJWTFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(InternalJWTFilter.class);
    private static final String INTERNAL_JWT_HEADER = "X-Internal-JWT";
    private final InternalJWTService internalJWTService;

    public InternalJWTFilter(InternalJWTService internalJWTService) {
        this.internalJWTService = internalJWTService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = request.getHeader(INTERNAL_JWT_HEADER);
        log.info("### INV Incoming X-Internal-JWT: {}", token);

        if (token != null && !token.isEmpty()) {
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(internalJWTService.getSecret())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String userId = claims.getSubject();
                Object rolesObj = claims.get("roles");
                List<String> roles = new ArrayList<>();

                if (rolesObj instanceof List<?>) {
                    for (Object role : (List<?>) rolesObj) {
                        if (role instanceof String) {
                            log.info("### role in inv : {}", role);
                            roles.add((String) role);
                        }
                    }
                }

                var authorities = roles.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());

                var auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                log.error("### INV Invalid internal JWT: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal JWT");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
