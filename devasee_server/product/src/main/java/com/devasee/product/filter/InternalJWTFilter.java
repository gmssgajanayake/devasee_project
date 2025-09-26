package com.devasee.product.filter;

import com.devasee.product.enums.AccountStatus;
import com.devasee.product.interfaces.UserClient;
import com.devasee.product.services.InternalJWTService;
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
    private final UserClient userClient;

    public InternalJWTFilter(
            InternalJWTService internalJWTService,
            UserClient userClient
    ) {
        this.internalJWTService = internalJWTService;
        this.userClient = userClient;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = request.getHeader(INTERNAL_JWT_HEADER);
        log.info("### Product Incoming X-Internal-JWT: {}", token);

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
                            log.info("### role in product : {}", role);
                            roles.add((String) role);
                        }
                    }
                }

//                // --- Fetch user from DB to check AccountStatus ---
//                String accountStatus = userClient.getUseAccountStatus(userId);
//                log.info("### accountStatus : {}", accountStatus);
//                if (!accountStatus.equals(AccountStatus.ACTIVE.name())) {
//                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your account is not active");
//                    return;
//                }

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
