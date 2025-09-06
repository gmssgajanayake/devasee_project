package com.devasee.product.interfaces;

import com.devasee.product.dto.InventoryRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory")
public interface InventoryClient {

    @GetMapping("/api/v1/inventory/admin/productId/{productId}")
    Integer getStockQuantity (@PathVariable("productId") String productId);

    @PostMapping("/api/v1/inventory/admin/addInventory")
    void createInventory(@RequestBody InventoryRequestDTO request);

}
