package com.devasee.product.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.devasee.product.exception.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class AzureBlobService {

    private static final Logger log = LoggerFactory.getLogger(AzureBlobService.class);

    private final BlobContainerClient containerClient;

    public AzureBlobService() {
        String connectStr = "DefaultEndpointsProtocol=https;EndpointSuffix=core.windows.net;AccountName=devaseebookstoreimages;AccountKey=pAjow61U0YsxPRwz+mZ+ihb3k4+KMrLHverA9Z4TY4nIwBS/MRjUDKUqD4EHQVGLhHmW2+/r/qGG+AStCiH9Kg==;BlobEndpoint=https://devaseebookstoreimages.blob.core.windows.net/;FileEndpoint=https://devaseebookstoreimages.file.core.windows.net/;QueueEndpoint=https://devaseebookstoreimages.queue.core.windows.net/;TableEndpoint=https://devaseebookstoreimages.table.core.windows.net/"; // replace with your Azure connection string
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectStr)
                .buildClient();
        containerClient = serviceClient.getBlobContainerClient("product-book-images");
    }

    // Method to store files in azure blob storage
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename  = file.getOriginalFilename();
        assert originalFilename != null;
        String extension = "";

        int dotIndex  = originalFilename.lastIndexOf('.');

        if (dotIndex  >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        // Generate random name with UUID
        String randomFilename = UUID.randomUUID().toString() + extension;

        BlobClient blobClient = containerClient.getBlobClient(randomFilename);
        blobClient.upload(file.getInputStream(), file.getSize(), true); // overwrite if exists

        // Return blob name
        log.info("### File uploading successfully : {}", randomFilename);
        return randomFilename;
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
            throw new ServiceUnavailableException("Failed to delete file from Azure");
        }
    }


}
