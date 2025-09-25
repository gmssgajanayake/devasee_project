package com.devasee.product.config;

import com.devasee.product.filter.InternalJWTFilter;
import com.devasee.product.interfaces.UserClient;
import com.devasee.product.services.InternalJWTService;
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
    private final UserClient userClient; // inject UserClient=

    public SecurityConfig(InternalJWTService internalJWTService, UserClient userClient) {
        this.internalJWTService = internalJWTService;
        this.userClient = userClient;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JsonAuthEntryPoint jsonAuthEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/product/books/**",
                                "/api/v1/product/books",
                                "/api/v1/product/stationery/**",
                                "/api/v1/product/stationery",
                                "/api/v1/product/printing/**",
                                "/api/v1/product/printing"
                                ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "api/v1/product/books",
                                "api/v1/product/books/**"
                                ).hasRole("ADMIN")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "api/v1/product/books"
                                ).hasRole("ADMIN")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "api/v1/product/books/**"
                                ).hasRole("ADMIN")
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
        return new InternalJWTFilter(internalJWTService, userClient);
    }
}
