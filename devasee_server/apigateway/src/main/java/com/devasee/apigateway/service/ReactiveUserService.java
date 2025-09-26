package com.devasee.apigateway.service;

import com.devasee.apigateway.UserDataDTO;
import reactor.core.publisher.Mono;


public interface ReactiveUserService {
      Mono<UserDataDTO> findRoleAccountStatusByUserId(String userId);
}
