package com.devasee.apigateway.config;

import com.devasee.apigateway.service.ReactiveUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private static final Logger log = LoggerFactory.getLogger(CustomJwtAuthenticationConverter.class);

    private final ReactiveUserService reactiveUserService;

    public CustomJwtAuthenticationConverter(ReactiveUserService reactiveUserService) {
        this.reactiveUserService = reactiveUserService;
    }


    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {

        String userId = jwt.getClaimAsString("userId"); // or "userId", whichever you use

        // Call your user service here asynchronously to get roles for the user
        // For example, assuming you have a ReactiveUserService returning Mono<List<String>>
        return reactiveUserService.findRolesByUserId(userId)
                .onErrorResume(e -> {
                    log.error("### Error fetching roles, using empty list: {}", e.getMessage());
                    return Mono.just(Collections.emptyList());
                })
                .map(roles -> {
                    var authorities = roles.stream()
                            .map(role -> "ROLE_" + role)
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    log.info("### User roles: {}", authorities);
                    return new JwtAuthenticationToken(jwt, authorities);
                });
    }
}
