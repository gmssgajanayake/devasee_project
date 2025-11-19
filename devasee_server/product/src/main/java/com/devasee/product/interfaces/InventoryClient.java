package com.devasee.product.interfaces;

import com.devasee.product.dto.stationery.InventoryRequestDTO;
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
