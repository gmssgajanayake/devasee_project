package com.devasee.delivery.services;

import com.devasee.delivery.entity.DeliveryPartner;
import com.devasee.delivery.exception.DeliveryNotFoundException;
import com.devasee.delivery.repo.DeliveryPartnerRepository;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Delivery Partner (courier companies)
 */
@Service
@RequiredArgsConstructor
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;

    /**
     * Get all couriers
     */
    public List<DeliveryPartner> getAllCouriers() {
        try {
            return deliveryPartnerRepository.findAll();
        } catch (Exception ex) {
            throw new DeliveryNotFoundException("Couriers not found");
        }
    }

    /**
     * Create a new courier
     */
    public DeliveryPartner createCourier(DeliveryPartner courier) {
        try {
            // Check duplicates by name or email or phone
            if (deliveryPartnerRepository.existsByName(courier.getName())) {
                throw new InternalServerErrorException("Courier already exists with name: " + courier.getName());
            }
            if (deliveryPartnerRepository.existsByEmail(courier.getEmail())) {
                throw new InternalServerErrorException("Courier already exists with email: " + courier.getEmail());
            }
            if (deliveryPartnerRepository.existsByPhone(courier.getPhone())) {
                throw new InternalServerErrorException("Courier already exists with phone: " + courier.getPhone());
            }

            return deliveryPartnerRepository.save(courier);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating courier: " + e.getMessage(), e);
        }
    }
}
