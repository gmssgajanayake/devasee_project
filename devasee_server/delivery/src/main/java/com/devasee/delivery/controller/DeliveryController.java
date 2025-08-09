package com.devasee.delivery.controller;

import com.devasee.delivery.dto.CreateDeliveryDTO;
import com.devasee.delivery.dto.DeleteDeliveryDTO;
import com.devasee.delivery.dto.RetrieveDeliveryDTO;
import com.devasee.delivery.response.CustomResponse;
import com.devasee.delivery.services.DeliveryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/add")
    public CustomResponse<CreateDeliveryDTO> addDelivery(@RequestBody CreateDeliveryDTO dto) {
        CreateDeliveryDTO created = deliveryService.addDelivery(dto);
        return new CustomResponse<>(true, "Delivery created successfully", created);
    }

    @GetMapping("/{id}")
    public CustomResponse<RetrieveDeliveryDTO> getDelivery(@PathVariable Long id) {
        RetrieveDeliveryDTO dto = deliveryService.getDeliveryById(id);
        return new CustomResponse<>(true, "Delivery found", dto);
    }

    @GetMapping("/all")
    public CustomResponse<List<RetrieveDeliveryDTO>> getAllDeliveries() {
        List<RetrieveDeliveryDTO> deliveries = deliveryService.getAllDeliveries();
        return new CustomResponse<>(true, "All deliveries fetched", deliveries);
    }

    @PutMapping("/update/{id}")
    public CustomResponse<RetrieveDeliveryDTO> updateDelivery(@PathVariable Long id, @RequestBody RetrieveDeliveryDTO dto) {
        RetrieveDeliveryDTO updated = deliveryService.updateDelivery(id, dto);
        return new CustomResponse<>(true, "Delivery updated successfully", updated);
    }

    @DeleteMapping("/delete/{id}")
    public CustomResponse<DeleteDeliveryDTO> deleteDelivery(@PathVariable Long id) {
        DeleteDeliveryDTO deleted = deliveryService.deleteDelivery(id);
        return new CustomResponse<>(true, "Delivery deleted successfully", deleted);
    }
}
