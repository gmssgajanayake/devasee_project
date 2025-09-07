package com.devasee.inventory.config;

import com.devasee.inventory.services.InternalJWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final InternalJWTService internalJWTService;

    public SecurityConfig(InternalJWTService internalJWTService) {
        this.internalJWTService = internalJWTService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "api/v1/inventory/product/*/quantity").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(internalJWTFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public InternalJWTFilter internalJWTFilter() {
        return new InternalJWTFilter(internalJWTService);
    }

    // Inner class version of the filter (can also stay as separate)
    public static class InternalJWTFilter extends OncePerRequestFilter {

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

            if(token != null && !token.isEmpty()){
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

                } catch (Exception e){
                    log.error("### INV Invalid internal JWT: {}", e.getMessage());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal JWT");
                    return;
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}
