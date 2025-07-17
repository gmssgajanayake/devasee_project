package org.devasee.promo.service;

import jakarta.transaction.Transactional;
import org.devasee.promo.dto.CreateAdsDTO;
import org.devasee.promo.dto.RetrieveAdsDTO;
import org.devasee.promo.model.Advertisement;
import org.devasee.promo.repo.AdsRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AdsService {

    private final AdsRepo adsRepo;
    private final ModelMapper modelMapper;

    public AdsService(
        AdsRepo adsRepo,
        ModelMapper modelMapper
    ){
        this.adsRepo = adsRepo;
        this.modelMapper = modelMapper;
    }

    public List<RetrieveAdsDTO> getAllAds(){
        List<Advertisement> advertisements = adsRepo.findAll();
        List<RetrieveAdsDTO> advertisementDTOS = modelMapper.map(advertisements,
                new TypeToken<List<RetrieveAdsDTO>>(){}.getType());

        return advertisementDTOS;
    }

    // Schedule an advertisement
    public CreateAdsDTO scheduleAds(CreateAdsDTO adDTO){
        Advertisement advertisement = modelMapper.map(adDTO, Advertisement.class);
        adsRepo.save(advertisement);

        return adDTO;
    }
}
