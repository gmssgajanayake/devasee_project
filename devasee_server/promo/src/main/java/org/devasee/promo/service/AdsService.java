package org.devasee.promo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.devasee.promo.dto.CreateAdsDTO;
import org.devasee.promo.dto.RetrieveAdsDTO;
import org.devasee.promo.model.Advertisement;
import org.devasee.promo.repo.AdsRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class AdsService {

    private final AdsRepo adsRepo;
    private final ModelMapper modelMapper;
    private final AzureBlobService azureBlobService;

    public AdsService(
        AdsRepo adsRepo,
        ModelMapper modelMapper,
        AzureBlobService azureBlobService
    ){
        this.adsRepo = adsRepo;
        this.modelMapper = modelMapper;
        this.azureBlobService = azureBlobService;
    }

    public List<RetrieveAdsDTO> getAllAds(){
        try {
            List<Advertisement> advertisements = adsRepo.findAll();

            // Convert Book → DTO and replace blob names with SAS URLs
            List<RetrieveAdsDTO> dtoList = advertisements.stream().map(this::sasUrlAdderAndQuantitySetter).toList();

            return dtoList;
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    // Due to image original url is private generate new SAS url
    // Same time add available quantity to response from Inventory
    private RetrieveAdsDTO sasUrlAdderAndQuantitySetter(Advertisement book){

        RetrieveAdsDTO dto = modelMapper.map(book, RetrieveAdsDTO.class);

        String blobName = dto.getImgUrl(); // stored as filename
        if (blobName != null && !blobName.isEmpty()) {
            String sasUrl = azureBlobService.generateSasUrl(blobName);
            dto.setImgUrl(sasUrl);
        }
        return dto;
    }

    // Schedule an advertisement
    public RetrieveAdsDTO scheduleAds(String adsJson, MultipartFile file){

        CreateAdsDTO adsDTO;

        try {
            // Convert JSON string to DTO
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            adsDTO = mapper.readValue(adsJson, CreateAdsDTO.class);

        } catch (Exception e){
            throw new RuntimeException("Error mapping JSON to DTO : "+ e);
        }

        try {
            Advertisement advertisement = modelMapper.map(adsDTO, Advertisement.class);

            String fileName = (file != null) ? azureBlobService.uploadFile(file) : null;
            advertisement.setImgUrl(fileName); // Set uploaded saved file's name as url

           Advertisement savedAdvertisement = adsRepo.save(advertisement);

            return modelMapper.map(savedAdvertisement, RetrieveAdsDTO.class);
        } catch (Exception e){
            throw new RuntimeException("Error");
        }
    }
}
