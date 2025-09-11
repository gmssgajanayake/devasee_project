package org.devasee.promo.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AzureBlobService {

    private static final Logger log = LoggerFactory.getLogger(AzureBlobService.class);

    private final BlobContainerClient containerClient;

    public AzureBlobService(
            @Value("${azure.storage.connect-str}") String connectStr
    ) {
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectStr)
                .buildClient();
        containerClient = serviceClient.getBlobContainerClient("promo-images");
    }

    // Method to store files in azure blob storage
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename  = file.getOriginalFilename();
        assert originalFilename != null;

        String namePart = "";
        String extension = "";

        int dotIndex  = originalFilename.lastIndexOf('.');

        if (dotIndex  >= 0) {
            namePart = originalFilename.substring(0, dotIndex); // part before the dot
            extension = originalFilename.substring(dotIndex);
        }

        // Add current date/time as string
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String newFilename = namePart + "_" + timestamp + extension;

        BlobClient blobClient = containerClient.getBlobClient(newFilename);
        blobClient.upload(file.getInputStream(), file.getSize(), true); // overwrite if exists

        // Return blob name
        log.info("### File uploading successfully : {}", newFilename);
        return newFilename;
    }

    // Generate SAS URL for a blob
    public String generateSasUrl(String blobName){

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(
                OffsetDateTime.now().plusHours(1), // SAS expiry time
                new BlobSasPermission().setReadPermission(true) // only read access
        );

        String sasToken = blobClient.generateSas(values);
        log.info("### SAS Url generated successfully");
        return blobClient.getBlobUrl()  + "?" +sasToken;
    }

    public void deleteFile(String blobName) {
        try {
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            if (blobClient.exists()) {
                blobClient.delete();
                log.info("### Blob deleted successfully: {}", blobName);
            }
        } catch (Exception e) {
            log.error("### Error deleting blob: {}", blobName, e);
            throw new RuntimeException("Failed to delete file from Azure");
        }
    }


}