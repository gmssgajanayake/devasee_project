package com.devasee.delivery.controller;

import com.devasee.delivery.dto.DeliveryDTO;
import com.devasee.delivery.services.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/add")
    public DeliveryDTO addDelivery(@RequestBody DeliveryDTO dto) {
        return deliveryService.addDelivery(dto);
    }

    @GetMapping("/{id}")
    public DeliveryDTO getDelivery(@PathVariable Long id) {
        return deliveryService.getDeliveryById(id);
    }

    @GetMapping("/all")
    public List<DeliveryDTO> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @PutMapping("/update/{id}")
    public DeliveryDTO updateDelivery(@PathVariable Long id, @RequestBody DeliveryDTO dto) {
        return deliveryService.updateDelivery(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteDelivery(@PathVariable Long id) {
        return deliveryService.deleteDelivery(id);
    }
}
