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

    // CREATE Delivery
    public CreateDeliveryDTO addDelivery(CreateDeliveryDTO dto) {
        try {
            Delivery delivery = new Delivery();
            delivery.setOrderId(dto.getOrderId());
            delivery.setAddress(dto.getAddress());
            delivery.setStatus(dto.getStatus());
            delivery.setDeliveryDate(dto.getDeliveryDate());

            // find courier by name
            Courier courier = courierRepository.findByNameIgnoreCase(dto.getCourierName())
                    .orElseThrow(() -> new DeliveryNotFoundException("Courier not found: " + dto.getCourierName()));
            delivery.setCourier(courier);

            Delivery saved = deliveryRepository.save(delivery);

            return new CreateDeliveryDTO(
                    saved.getOrderId(),
                    saved.getAddress(),
                    saved.getStatus(),
                    saved.getCourier().getName(),
                    saved.getDeliveryDate()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while adding delivery: " + e.getMessage(), e);
        }
    }

    // READ by ID
    public RetrieveDeliveryDTO getDeliveryById(String id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with ID: " + id));

        return new RetrieveDeliveryDTO(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getAddress(),
                delivery.getStatus(),
                delivery.getCourier().getName(),
                delivery.getDeliveryDate()
        );
    }

    // READ all with paging
    public Page<RetrieveDeliveryDTO> getAllDeliveries(Pageable pageable) {
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);

        return deliveries.map(delivery -> new RetrieveDeliveryDTO(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getAddress(),
                delivery.getStatus(),
                delivery.getCourier().getName(),
                delivery.getDeliveryDate()
        ));
    }

    // UPDATE Delivery
    public RetrieveDeliveryDTO updateDelivery(String id, RetrieveDeliveryDTO dto) {
        Delivery existing = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Cannot update. Delivery not found with ID: " + id));

        existing.setOrderId(dto.getOrderId());
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
                saved.getId(),
                saved.getOrderId(),
                saved.getAddress(),
                saved.getStatus(),
                saved.getCourier().getName(),
                saved.getDeliveryDate()
        );
    }

    // DELETE Delivery
    public DeleteDeliveryDTO deleteDelivery(String id) {
        Delivery existing = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Cannot delete. Delivery not found with ID: " + id));

        deliveryRepository.delete(existing);

        return new DeleteDeliveryDTO(
                existing.getId(),
                existing.getAddress(),
                existing.getStatus(),
                existing.getCourier().getName()
        );
    }

    // DELIVERY Stats
    public DeliveryStatsDTO calculateDeliveryStats() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        int totalDeliveries = deliveries.size();
        int pendingDeliveries = (int) deliveries.stream()
                .filter(d -> DeliveryStatus.PENDING.equals(d.getStatus()))
                .count();
        return new DeliveryStatsDTO(totalDeliveries, pendingDeliveries);
    }

    // GET all statuses
    public List<String> getAllStatuses() {
        return List.of(
                DeliveryStatus.PENDING.name(),
                DeliveryStatus.IN_TRANSIT.name(),
                DeliveryStatus.DELIVERED.name()
        );
    }


}
