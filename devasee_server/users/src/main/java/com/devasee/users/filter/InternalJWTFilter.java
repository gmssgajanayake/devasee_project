package com.devasee.users.filter;

import com.devasee.users.service.InternalJWTService;
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

        String path = request.getRequestURI();

        String internalJwt = request.getHeader(INTERNAL_JWT_HEADER);
        log.info("### User Incoming X-Internal-JWT: {}", internalJwt);

        // Skip for internal roles endpoint (validate only internal JWT if present)\
        if (path.startsWith("/api/v1/users/auth/") && path.endsWith("/roles")) {
            log.info("### Dummy Authentication Creating");
            var auth = new UsernamePasswordAuthenticationToken("internal-service", null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);

            // allow request to pass
            filterChain.doFilter(request, response);
            return;
        }

        if (internalJwt != null && !internalJwt.isEmpty()) {
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(internalJWTService.getSecret())
                        .build()
                        .parseClaimsJws(internalJwt)
                        .getBody();

                String userId = claims.getSubject();
                Object rolesObj = claims.get("roles");
                List<String> roles = new ArrayList<>();

                if (rolesObj instanceof List<?>) {
                    for (Object role : (List<?>) rolesObj) {
                        if (role instanceof String) {
                            log.info("### role in user : {}", role);
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
