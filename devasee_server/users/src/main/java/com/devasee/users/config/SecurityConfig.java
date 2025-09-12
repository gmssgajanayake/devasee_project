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
                        .requestMatchers(
                                "api/v1/users/admin/**"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(internalJWTFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public InternalJWTFilter internalJWTFilter() {
        return new InternalJWTFilter(internalJWTService);
    }
}

