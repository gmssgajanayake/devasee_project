package org.devasee.promo.controller;

import lombok.RequiredArgsConstructor;
import org.devasee.promo.dto.CreateAdsDTO;
import org.devasee.promo.dto.UpdateAdsDTO;
import org.devasee.promo.dto.RetrieveAdsDTO;
import org.devasee.promo.dto.DeleteAdsDTO;
import org.devasee.promo.response.CustomResponse;
import org.devasee.promo.service.AdsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST Controller for managing Promo Advertisements
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1/promo")
@RequiredArgsConstructor
public class PromoController {

    private final AdsService adsService;

    /**
     * Fetch all advertisements (Public endpoint)
     */
    @GetMapping
    public CustomResponse<List<RetrieveAdsDTO>> getAllAds() {
        List<RetrieveAdsDTO> ads = adsService.getAllAds();
        return new CustomResponse<>(true, "Advertisements found", ads);
    }

    /**
     * Create a new advertisement (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomResponse<RetrieveAdsDTO> createAds(
            @ModelAttribute CreateAdsDTO dto, // form-data DTO binding
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        RetrieveAdsDTO savedAd = adsService.createAds(dto, file);
        return new CustomResponse<>(true, "Advertisement created successfully", savedAd);
    }

    /**
     * Update an advertisement (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public CustomResponse<RetrieveAdsDTO> updateAds(
            @ModelAttribute UpdateAdsDTO dto, // form-data DTO binding
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        RetrieveAdsDTO updatedAd = adsService.updateAds(dto, file);
        return new CustomResponse<>(true, "Advertisement updated successfully", updatedAd);
    }

    /**
     * Delete an advertisement (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public CustomResponse<DeleteAdsDTO> deleteAds(@PathVariable String id) {
        DeleteAdsDTO deletedAd = adsService.deleteAds(id);
        return new CustomResponse<>(true, "Advertisement deleted successfully", deletedAd);
    }
}
