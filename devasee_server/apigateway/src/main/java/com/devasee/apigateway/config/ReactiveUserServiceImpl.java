package com.devasee.apigateway.config;

import com.devasee.apigateway.UserDataDTO;
import com.devasee.apigateway.service.ReactiveUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class ReactiveUserServiceImpl implements ReactiveUserService {

    private final WebClient webClient;

    @Value("${services.user-service.base-url}")
    private String userServiceBaseUrl;

    public ReactiveUserServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<UserDataDTO> findRoleAccountStatusByUserId(String userId) {
        return webClient.get()
                .uri(userServiceBaseUrl + "/{userId}/info", userId)
                .retrieve()
                .bodyToMono(
                        new org.springframework.core.ParameterizedTypeReference
                                <>(){}
                );
    }
}
