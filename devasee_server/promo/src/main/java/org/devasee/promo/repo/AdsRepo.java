package org.devasee.promo.repo;

import org.devasee.promo.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdsRepo extends JpaRepository<Advertisement, String> {
}
