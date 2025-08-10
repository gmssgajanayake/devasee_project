package com.devasee.apigateway.config;

import com.devasee.apigateway.service.ReactiveUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ReactiveUserServiceImpl implements ReactiveUserService {

    private final WebClient webClient;

    @Value("${services.user-service.base-url}")
    private String userServiceBaseUrl;

    public ReactiveUserServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<List<String>> findRolesByUserId(String userId) {
        return webClient.get()
                .uri(userServiceBaseUrl + "/{userId}/roles", userId)
                .retrieve()
                .bodyToMono(
                        new org.springframework.core.ParameterizedTypeReference
                                <List<String>>(){}
                );
    }
//
//    @Bean
//    @LoadBalanced
//    public WebClient.Builder webClientBuilder() {
//        return WebClient.builder();
//    }
}
