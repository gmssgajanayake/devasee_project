package com.devasee.delivery.controller;

import com.devasee.delivery.dto.CreateDeliveryDTO;
import com.devasee.delivery.dto.DeleteDeliveryDTO;
import com.devasee.delivery.dto.DeliveryStatsDTO;
import com.devasee.delivery.dto.RetrieveDeliveryDTO;
import com.devasee.delivery.entity.Courier;
import com.devasee.delivery.response.CustomResponse;
import com.devasee.delivery.services.CourierService;
import com.devasee.delivery.services.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final CourierService courierService;
    /**
     * Fetch all deliveries (Accessible by ADMIN )
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public CustomResponse<Page<RetrieveDeliveryDTO>> getAllDeliveries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RetrieveDeliveryDTO> deliveries = deliveryService.getAllDeliveries(pageable);
        return new CustomResponse<>(true, "All deliveries fetched", deliveries);
    }

    // =================== Delivery endpoints ===================

    /**
     * Fetch a single delivery by ID (Accessible by ADMIN)
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public CustomResponse<RetrieveDeliveryDTO> getDelivery(@PathVariable String id) {
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
            @PathVariable String id,
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
    public CustomResponse<DeleteDeliveryDTO> deleteDelivery(@PathVariable String id) {
        DeleteDeliveryDTO deleted = deliveryService.deleteDelivery(id);
        return new CustomResponse<>(true, "Delivery deleted successfully", deleted);
    }

    /**
     * Fetch delivery statistics (Accessible by ADMIN)
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


    // =================== Courier endpoints ===================

    /**
     * Fetch all courier services (Accessible by ADMIN)
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/couriers")
    public CustomResponse<List<Courier>> getAllCouriers() {
        List<Courier> couriers = courierService.getAllCouriers();
        return new CustomResponse<>(true, "All couriers fetched", couriers);
    }

    /**
     * Create a courier (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/couriers")
    public CustomResponse<Courier> createCourier(@RequestParam String courierName) {
        Courier courier = courierService.createCourier(courierName);
        return new CustomResponse<>(true, "Courier created successfully", courier);
    }
}
