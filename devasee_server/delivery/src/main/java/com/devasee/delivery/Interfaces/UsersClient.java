package com.devasee.delivery.Interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users", path = "/api/v1/users/admins")
public interface UsersClient {
    @GetMapping("/{userId}/name")
    String getAdminNameById(@PathVariable("userId") String userId);
}
