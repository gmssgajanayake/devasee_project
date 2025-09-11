package org.devasee.promo.controller;

import org.devasee.promo.dto.RetrieveAdsDTO;
import org.devasee.promo.response.CustomResponse;
import org.devasee.promo.service.AdsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/promo")
public class PromoController {

    private final AdsService adsService;

    public PromoController(
            AdsService adsService
    ){
        this.adsService = adsService;
    }

    @GetMapping
    public CustomResponse<List<RetrieveAdsDTO>> getAllAds(){
        List<RetrieveAdsDTO> promoAds = adsService.getAllAds();
        return new CustomResponse<>(true, "Promo Advertisements Retrieved Successfully", promoAds);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CustomResponse<RetrieveAdsDTO> createAds(
            @RequestParam("advertisement") String adsJson,
            @RequestParam("file") MultipartFile file
    ){
        RetrieveAdsDTO dto = adsService.scheduleAds(adsJson, file);
        return new CustomResponse<>(true, "Promo Advertisement Saved Successfully", dto);
    }
}
