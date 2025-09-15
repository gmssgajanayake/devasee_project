package com.devasee.delivery.controller;

import com.devasee.delivery.dto.CreateDeliveryDTO;
import com.devasee.delivery.dto.DeleteDeliveryDTO;
import com.devasee.delivery.dto.DeliveryStatsDTO;
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

    // CREATE
    @PostMapping("/add")
    public CustomResponse<CreateDeliveryDTO> addDelivery(@RequestBody CreateDeliveryDTO dto) {
        CreateDeliveryDTO created = deliveryService.addDelivery(dto);
        return new CustomResponse<>(true, "Delivery created successfully", created);
    }

    // READ single
    @GetMapping("/{id}")
    public CustomResponse<RetrieveDeliveryDTO> getDelivery(@PathVariable Long id) {
        RetrieveDeliveryDTO dto = deliveryService.getDeliveryById(id);
        return new CustomResponse<>(true, "Delivery found", dto);
    }

    // READ all
    @GetMapping("/all")
    public CustomResponse<List<RetrieveDeliveryDTO>> getAllDeliveries() {
        List<RetrieveDeliveryDTO> deliveries = deliveryService.getAllDeliveries();
        return new CustomResponse<>(true, "All deliveries fetched", deliveries);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public CustomResponse<RetrieveDeliveryDTO> updateDelivery(@PathVariable Long id, @RequestBody RetrieveDeliveryDTO dto) {
        RetrieveDeliveryDTO updated = deliveryService.updateDelivery(id, dto);
        return new CustomResponse<>(true, "Delivery updated successfully", updated);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public CustomResponse<DeleteDeliveryDTO> deleteDelivery(@PathVariable Long id) {
        DeleteDeliveryDTO deleted = deliveryService.deleteDelivery(id);
        return new CustomResponse<>(true, "Delivery deleted successfully", deleted);
    }

    // DELIVERY STATS
    @GetMapping("/stats")
    public CustomResponse<DeliveryStatsDTO> getDeliveryStats() {
        DeliveryStatsDTO stats = deliveryService.calculateDeliveryStats();
        return new CustomResponse<>(true, "Delivery statistics fetched", stats);
    }

    // GET all delivery statuses (enum values)
    @GetMapping("/statuses")
    public CustomResponse<List<String>> getAllStatuses() {
        List<String> statuses = deliveryService.getAllStatuses();
        return new CustomResponse<>(true, "Delivery statuses fetched", statuses);
    }

    // GET all courier services (enum values)
    @GetMapping("/couriers")
    public CustomResponse<List<String>> getAllCourierServices() {
        List<String> couriers = deliveryService.getAllCourierServices();
        return new CustomResponse<>(true, "Courier services fetched", couriers);
    }
}
