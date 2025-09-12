package com.devasee.product.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.devasee.product.enums.ContainerType;
import com.devasee.product.exception.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class AzureBlobService {

    private static final Logger log = LoggerFactory.getLogger(AzureBlobService.class);

    private final Map<ContainerType, BlobContainerClient> containers = new HashMap<>();

    public AzureBlobService(
            @Value("${azure.storage.connect-str}") String connectStr
    ) {
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectStr)
                .buildClient();

        for (ContainerType type : ContainerType.values()) {
            BlobContainerClient client = serviceClient.getBlobContainerClient(type.getContainerName());
            if (!client.exists()) {
                try {
                    client.create();
                } catch (Exception e) {
                    log.warn("Container {} creation skipped: {}", type.getContainerName(), e.getMessage());
                }
            }
            containers.put(type, client);
        }
    }

    // Method to store files in azure blob storage
    public String uploadFile(MultipartFile file, ContainerType containerType) throws IOException {

        BlobContainerClient containerClient = containers.get(containerType);
        if (containerClient == null) {
            throw new IllegalArgumentException("Container not found: " + containerType);
        }

        String originalFilename  = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("File must have a name");
        }

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
        log.info("### Uploaded file '{}' to container '{}'", newFilename, containerType.getContainerName());
        return newFilename;
    }

    // Generate SAS URL for a blob
    public String generateSasUrl(String blobName, ContainerType  containerType){

        BlobContainerClient containerClient = containers.get(containerType);
        if (containerClient == null) {
            throw new IllegalArgumentException("Container not found: " + containerType);
        }

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(
                OffsetDateTime.now().plusHours(1), // SAS expiry time
                new BlobSasPermission().setReadPermission(true) // only read access
        );

        String sasToken = blobClient.generateSas(values);
        log.info("### Generated SAS URL for blob '{}' in container '{}'", blobName, containerType.getContainerName());
        return blobClient.getBlobUrl()  + "?" +sasToken;
    }

    public void deleteFile(String blobName, ContainerType containerType) {

        BlobContainerClient containerClient = containers.get(containerType);
        if (containerClient == null) {
            throw new IllegalArgumentException("Container not found: " + containerType );
        }

        try {
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            if (blobClient.exists()) {
                blobClient.delete();
                log.info("### Deleted blob '{}' from container '{}'", blobName, containerType.getContainerName());
            }
        } catch (Exception e) {
            log.error("### Error deleting blob '{}' from container '{}': {}", blobName, containerType.getContainerName(), e.getMessage());
            throw new ServiceUnavailableException("Failed to delete file from Azure");
        }
    }
}
