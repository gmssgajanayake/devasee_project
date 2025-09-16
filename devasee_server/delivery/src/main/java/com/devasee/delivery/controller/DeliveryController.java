package com.devasee.delivery.controller;

import com.devasee.delivery.dto.CreateDeliveryDTO;
import com.devasee.delivery.dto.DeleteDeliveryDTO;
import com.devasee.delivery.dto.DeliveryStatsDTO;
import com.devasee.delivery.dto.RetrieveDeliveryDTO;
import com.devasee.delivery.response.CustomResponse;
import com.devasee.delivery.services.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Deliveries
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * Fetch all deliveries (Accessible by ADMIN and USER)
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public CustomResponse<List<RetrieveDeliveryDTO>> getAllDeliveries() {
        List<RetrieveDeliveryDTO> deliveries = deliveryService.getAllDeliveries();
        return new CustomResponse<>(true, "All deliveries fetched", deliveries);
    }

    /**
     * Fetch a single delivery by ID (Accessible by ADMIN and USER)
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public CustomResponse<RetrieveDeliveryDTO> getDelivery(@PathVariable Long id) {
        RetrieveDeliveryDTO dto = deliveryService.getDeliveryById(id);
        return new CustomResponse<>(true, "Delivery found", dto);
    }

    /**
     * Create a new delivery (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomResponse<CreateDeliveryDTO> addDelivery(@RequestBody CreateDeliveryDTO dto) {
        CreateDeliveryDTO created = deliveryService.addDelivery(dto);
        return new CustomResponse<>(true, "Delivery created successfully", created);
    }

    /**
     * Update an existing delivery (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CustomResponse<RetrieveDeliveryDTO> updateDelivery(
            @PathVariable Long id,
            @RequestBody RetrieveDeliveryDTO dto
    ) {
        RetrieveDeliveryDTO updated = deliveryService.updateDelivery(id, dto);
        return new CustomResponse<>(true, "Delivery updated successfully", updated);
    }

    /**
     * Delete a delivery (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public CustomResponse<DeleteDeliveryDTO> deleteDelivery(@PathVariable Long id) {
        DeleteDeliveryDTO deleted = deliveryService.deleteDelivery(id);
        return new CustomResponse<>(true, "Delivery deleted successfully", deleted);
    }

    /**
     * Fetch delivery statistics (Accessible by ADMIN and USER)
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/stats")
    public CustomResponse<DeliveryStatsDTO> getDeliveryStats() {
        DeliveryStatsDTO stats = deliveryService.calculateDeliveryStats();
        return new CustomResponse<>(true, "Delivery statistics fetched", stats);
    }

    /**
     * Fetch all delivery statuses (Accessible by ADMIN and USER)
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/statuses")
    public CustomResponse<List<String>> getAllStatuses() {
        List<String> statuses = deliveryService.getAllStatuses();
        return new CustomResponse<>(true, "Delivery statuses fetched", statuses);
    }

    /**
     * Fetch all courier services (Accessible by ADMIN and USER)
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/couriers")
    public CustomResponse<List<String>> getAllCourierServices() {
        List<String> couriers = deliveryService.getAllCourierServices();
        return new CustomResponse<>(true, "Courier services fetched", couriers);
    }
}
