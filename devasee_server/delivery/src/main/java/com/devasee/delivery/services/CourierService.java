package com.devasee.delivery.services;

import com.devasee.delivery.entity.Courier;
import com.devasee.delivery.exception.DeliveryNotFoundException;
import com.devasee.delivery.repo.CourierRepository;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;

    // Get all couriers
    public List<Courier> getAllCouriers() {
        try {
            return courierRepository.findAll();
        } catch (Exception ex) {
            throw new DeliveryNotFoundException("Couriers not found");
        }
    }

    // Create courier
    public Courier createCourier(String courierName) {
        if (courierRepository.existsByName(courierName)) {
            throw new InternalServerErrorException("Courier already exists: " + courierName);
        }

        Courier courier = new Courier();
        courier.setName(courierName);

        try {
            return courierRepository.save(courier);
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error saving courier");
        }
    }
}
