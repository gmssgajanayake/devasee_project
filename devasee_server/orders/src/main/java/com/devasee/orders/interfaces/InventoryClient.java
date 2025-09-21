package com.devasee.orders.interfaces;

import com.devasee.orders.dto.InventoryRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory")
public interface InventoryClient {

    @GetMapping("/api/v1/inventory/product/{productId}/quantity")
    Integer getStockQuantity (@PathVariable("productId") String productId);

    @PostMapping("/api/v1/inventory")
    void createInventory(@RequestBody InventoryRequestDTO request);

    @DeleteMapping("/api/v1/inventory/product/{productId}")
    void deleteInventory(@PathVariable String productId);

}
