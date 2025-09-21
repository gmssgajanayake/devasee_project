package com.devasee.orders.interfaces;

import com.devasee.orders.dto.CreateDeliveryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "delivery")
public interface DeliveryClient {

    @PostMapping("/api/v1/delivery")
    void createDelivery(@RequestBody CreateDeliveryDTO request);

}
