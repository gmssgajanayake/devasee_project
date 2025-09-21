package com.devasee.product.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users")
public interface UserClient {
    @GetMapping("/api/v1/users/admins/internal/{userId}/status")
    String getUseAccountStatus (@PathVariable("userId") String userId);
}
