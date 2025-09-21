package com.devasee.delivery.services;

import com.devasee.delivery.dto.CreateDeliveryDTO;
import com.devasee.delivery.dto.DeleteDeliveryDTO;
import com.devasee.delivery.dto.DeliveryStatsDTO;
import com.devasee.delivery.dto.RetrieveDeliveryDTO;
import com.devasee.delivery.entity.Courier;
import com.devasee.delivery.entity.Delivery;
import com.devasee.delivery.enums.DeliveryStatus;
import com.devasee.delivery.exception.DeliveryNotFoundException;
import com.devasee.delivery.repo.CourierRepository;
import com.devasee.delivery.repo.DeliveryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final CourierRepository courierRepository;

    public DeliveryService(DeliveryRepository deliveryRepository,
                           CourierRepository courierRepository) {
        this.deliveryRepository = deliveryRepository;
        this.courierRepository = courierRepository;
    }

    /**
     * CREATE a new Delivery
     */
    public CreateDeliveryDTO addDelivery(CreateDeliveryDTO dto) {
        try {
            Delivery delivery = new Delivery();
            delivery.setOrderId(dto.getOrderId());
            delivery.setProductId(dto.getProductId());
            delivery.setOrderQuantity(dto.getOrderQuantity());
            delivery.setStatus(dto.getStatus());

            // Assign a courier (you could also add courierName in DTO if needed)
            Courier courier = courierRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new DeliveryNotFoundException("No courier available"));
            delivery.setCourier(courier);

            // Default: delivery date = today
            delivery.setDeliveryDate(java.time.LocalDate.now());

            // Persist
            Delivery saved = deliveryRepository.save(delivery);

            // Return same DTO back
            return new CreateDeliveryDTO(
                    saved.getOrderId(),
                    saved.getProductId(),
                    saved.getStatus(),
                    saved.getOrderQuantity()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while adding delivery: " + e.getMessage(), e);
        }
    }

    /**
     * READ Delivery by ID
     */
    public RetrieveDeliveryDTO getDeliveryById(String id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with ID: " + id));

        return new RetrieveDeliveryDTO(
                delivery.getDeliveryId(),
                delivery.getOrderId(),
                delivery.getProductId(),
                delivery.getAddress(),
                delivery.getStatus(),
                delivery.getOrderQuantity(),
                delivery.getCourier().getName(),
                delivery.getDeliveryDate()
        );
    }

    /**
     * READ all Deliveries with paging
     */
    public Page<RetrieveDeliveryDTO> getAllDeliveries(Pageable pageable) {
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);

        return deliveries.map(delivery -> new RetrieveDeliveryDTO(
                delivery.getDeliveryId(),
                delivery.getOrderId(),
                delivery.getProductId(),
                delivery.getAddress(),
                delivery.getStatus(),
                delivery.getOrderQuantity(),
                delivery.getCourier().getName(),
                delivery.getDeliveryDate()
        ));
    }

    /**
     * UPDATE Delivery by ID
     */
    public RetrieveDeliveryDTO updateDelivery(String id, RetrieveDeliveryDTO dto) {
        Delivery existing = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Cannot update. Delivery not found with ID: " + id));

        existing.setOrderId(dto.getOrderId());
        existing.setProductId(dto.getProductId());
        existing.setOrderQuantity(dto.getOrderQuantity());
        existing.setAddress(dto.getAddress());
        existing.setStatus(dto.getStatus());
        existing.setDeliveryDate(dto.getDeliveryDate());

        if (dto.getCourierName() != null) {
            Courier courier = courierRepository.findByNameIgnoreCase(dto.getCourierName())
                    .orElseThrow(() -> new DeliveryNotFoundException("Courier not found: " + dto.getCourierName()));
            existing.setCourier(courier);
        }

        Delivery saved = deliveryRepository.save(existing);

        return new RetrieveDeliveryDTO(
                saved.getDeliveryId(),
                saved.getOrderId(),
                saved.getProductId(),
                saved.getAddress(),
                saved.getStatus(),
                saved.getOrderQuantity(),
                saved.getCourier().getName(),
                saved.getDeliveryDate()
        );
    }

    /**
     * DELETE Delivery by ID
     */
    public DeleteDeliveryDTO deleteDelivery(String id) {
        Delivery existing = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Cannot delete. Delivery not found with ID: " + id));

        deliveryRepository.delete(existing);

        return new DeleteDeliveryDTO(
                existing.getDeliveryId(),
                existing.getAddress(),
                existing.getStatus(),
                existing.getCourier().getName()
        );
    }

    /**
     * DELIVERY statistics (total + pending count)
     */
    public DeliveryStatsDTO calculateDeliveryStats() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        int totalDeliveries = deliveries.size();
        int pendingDeliveries = (int) deliveries.stream()
                .filter(d -> DeliveryStatus.PENDING.equals(d.getStatus()))
                .count();
        return new DeliveryStatsDTO(totalDeliveries, pendingDeliveries);
    }

    /**
     * Get all possible delivery statuses
     */
    public List<String> getAllStatuses() {
        return List.of(
                DeliveryStatus.PENDING.name(),
                DeliveryStatus.IN_TRANSIT.name(),
                DeliveryStatus.DELIVERED.name()
        );
    }
}
