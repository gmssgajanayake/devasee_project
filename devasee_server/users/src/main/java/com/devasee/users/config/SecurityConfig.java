package com.devasee.users.config;


import com.devasee.users.filter.InternalJWTFilter;
import com.devasee.users.service.InternalJWTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Allows us to use annotations like @PreAuthorize
public class SecurityConfig {

    private final InternalJWTService internalJWTService;

    public SecurityConfig(InternalJWTService internalJWTService) {
        this.internalJWTService = internalJWTService;
    }

    // Define Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "api/v1/users/admin/**"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated()) // All other requests → must be authenticated (internal JWT must exist)
                .addFilterBefore(internalJWTFilter(), UsernamePasswordAuthenticationFilter.class);
                // Above adding custom filter (InternalJWTFilter) before UsernamePasswordAuthenticationFilter

        return http.build();
    }

    @Bean
    public InternalJWTFilter internalJWTFilter() {
        return new InternalJWTFilter(internalJWTService);
    }
}

