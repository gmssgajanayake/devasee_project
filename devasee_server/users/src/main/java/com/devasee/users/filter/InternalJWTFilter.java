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

// This filter ensures that any request coming from the API Gateway with X-Internal-JWT is authenticated and can carry
// user roles for authorization.

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

        // Retrieves the internal JWT added by the API Gateway.
        String internalJwt = request.getHeader(INTERNAL_JWT_HEADER);
        log.info("### User Incoming X-Internal-JWT: {}", internalJwt);

        // Skip for internal roles endpoint (validate only internal JWT if present)
        // Certain internal endpoints may need a dummy authentication for internal service calls
        //Sets a placeholder authentication object
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
                // Parse & validate JWT
                // validates signature and ensures token is not expired.
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

                // Convert roles to Spring authorities
                var authorities = roles.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());

                // Set authentication in SecurityContext
                // Populates Spring Security context with authenticated user
                // Now, endpoint access checks (hasRole("ADMIN"), @PreAuthorize) will work based on this authentication
                var auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                // Handle invalid JWT
                log.error("### INV Invalid internal JWT: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal JWT");
                return;
            }
        }

        /// “I’m done with my work (e.g., validating the JWT and setting the SecurityContext). Now let the rest of the
        /// filters handle the request, and eventually reach the controller.”
        filterChain.doFilter(request, response);
    }
}
