package com.devasee.apigateway.service;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ReactiveUserService {
      Mono<List<String>> findRolesByUserId(String userId);
}
