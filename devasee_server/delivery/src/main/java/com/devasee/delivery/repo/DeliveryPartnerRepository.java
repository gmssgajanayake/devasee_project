package com.devasee.delivery.repo;

import com.devasee.delivery.entity.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, String> {
    boolean existsByName(String name);
    Optional<DeliveryPartner> findByNameIgnoreCase(String name);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
