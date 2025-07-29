package org.devasee.promo.controller;

import org.devasee.promo.dto.CreateAdsDTO;
import org.devasee.promo.dto.RetrieveAdsDTO;
import org.devasee.promo.service.AdsService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/public/ads")
    public List<RetrieveAdsDTO> getAllAds(){
        return adsService.getAllAds();
    }

    @PostMapping("/createAds")
    public CreateAdsDTO createAds(@RequestBody CreateAdsDTO adDTO){
        return adsService.scheduleAds(adDTO);
    }
}
