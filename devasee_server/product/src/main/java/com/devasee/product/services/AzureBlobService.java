package com.devasee.product.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AzureBlobService {

    private final BlobContainerClient containerClient;

    public AzureBlobService() {
        String connectStr = "DefaultEndpointsProtocol=https;EndpointSuffix=core.windows.net;AccountName=devaseebookstoreimages;AccountKey=pAjow61U0YsxPRwz+mZ+ihb3k4+KMrLHverA9Z4TY4nIwBS/MRjUDKUqD4EHQVGLhHmW2+/r/qGG+AStCiH9Kg==;BlobEndpoint=https://devaseebookstoreimages.blob.core.windows.net/;FileEndpoint=https://devaseebookstoreimages.file.core.windows.net/;QueueEndpoint=https://devaseebookstoreimages.queue.core.windows.net/;TableEndpoint=https://devaseebookstoreimages.table.core.windows.net/"; // replace with your Azure connection string
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectStr)
                .buildClient();
        containerClient = serviceClient.getBlobContainerClient("product-book-images");
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        assert filename != null;
        BlobClient blobClient = containerClient.getBlobClient(filename);
        blobClient.upload(file.getInputStream(), file.getSize(), true); // overwrite if exists
        return blobClient.getBlobUrl(); // Returns the URL of uploaded file
    }

}
