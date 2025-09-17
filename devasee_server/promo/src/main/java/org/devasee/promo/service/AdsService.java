package org.devasee.promo.service;

import lombok.RequiredArgsConstructor;
import org.devasee.promo.dto.CreateAdsDTO;
import org.devasee.promo.dto.RetrieveAdsDTO;
import org.devasee.promo.dto.UpdateAdsDTO;
import org.devasee.promo.dto.DeleteAdsDTO;
import org.devasee.promo.model.Advertisement;
import org.devasee.promo.exception.AdsNotFoundException;
import org.devasee.promo.exception.ServiceUnavailableException;
import org.devasee.promo.repo.AdsRepo;
import org.devasee.promo.service.AzureBlobService;
import org.devasee.promo.service.InternalJWTService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for handling Advertisement business logic
 */
@Service
@RequiredArgsConstructor
public class AdsService {

    private final AdsRepo adsRepo;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;
    private final InternalJWTService internalJWTService; // useful for validating tokens if needed

    /**
     * Create a new advertisement
     */
    public RetrieveAdsDTO createAds(CreateAdsDTO dto, MultipartFile file) {
        try {
            Advertisement ad = modelMapper.map(dto, Advertisement.class);

            // Upload image if provided
            if (file != null && !file.isEmpty()) {
                String fileName = azureBlobService.uploadFile(file);
                ad.setImgUrl(fileName);
            }

            Advertisement savedAd = adsRepo.save(ad);
            return modelMapper.map(savedAd, RetrieveAdsDTO.class);

        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to create advertisement: " + e.getMessage());
        }
    }

    /**
     * Retrieve all advertisements
     */
    public List<RetrieveAdsDTO> getAllAds() {
        try {
            List<Advertisement> ads = adsRepo.findAll();
            return ads.stream()
                    .map(ad -> modelMapper.map(ad, RetrieveAdsDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to fetch advertisements: " + e.getMessage());
        }
    }

    /**
     * Update an existing advertisement
     */
    public RetrieveAdsDTO updateAds(UpdateAdsDTO dto, MultipartFile file) {
        try {
            Advertisement ad = adsRepo.findById(dto.getId())
                    .orElseThrow(() -> new AdsNotFoundException("Advertisement not found with ID: " + dto.getId()));

            // Update fields
            ad.setTitle(dto.getTitle());
            ad.setDescription(dto.getDescription());
            ad.setStartDate(dto.getStartDate());
            ad.setEndDate(dto.getEndDate());

            // Replace file if provided
            if (file != null && !file.isEmpty()) {
                if (ad.getImgUrl() != null) {
                    azureBlobService.deleteFile(ad.getImgUrl()); // remove old image
                }
                String newFileName = azureBlobService.uploadFile(file);
                ad.setImgUrl(newFileName);
            }

            Advertisement updatedAd = adsRepo.save(ad);
            return modelMapper.map(updatedAd, RetrieveAdsDTO.class);

        } catch (AdsNotFoundException e) {
            throw e; // let GlobalExceptionHandler handle
        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to update advertisement: " + e.getMessage());
        }
    }

    /**
     * Delete an advertisement
     */
    public DeleteAdsDTO deleteAds(String id) {
        try {
            Advertisement ad = adsRepo.findById(id)
                    .orElseThrow(() -> new AdsNotFoundException("Advertisement not found with ID: " + id));

            // Delete blob if exists
            if (ad.getImgUrl() != null) {
                azureBlobService.deleteFile(ad.getImgUrl());
            }

            adsRepo.delete(ad);
            return new DeleteAdsDTO(id, "Advertisement deleted successfully");

        } catch (AdsNotFoundException e) {
            throw e; // handled by GlobalExceptionHandler
        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to delete advertisement: " + e.getMessage());
        }
    }
}
