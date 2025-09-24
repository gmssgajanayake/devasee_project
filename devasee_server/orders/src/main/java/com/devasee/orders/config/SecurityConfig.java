package com.devasee.orders.config;

import com.devasee.orders.filter.InternalJWTFilter;
import com.devasee.orders.services.InternalJWTService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JsonAuthEntryPoint jsonAuthEntryPoint,
                                           CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/orders/order/**",
                                "/api/v1/orders/order"
                        ).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jsonAuthEntryPoint) // 401
                        .accessDeniedHandler(customAccessDeniedHandler) // 403
                )
                .addFilterBefore(internalJWTFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public InternalJWTFilter internalJWTFilter() {
        return new InternalJWTFilter(internalJWTService);
    }
}
