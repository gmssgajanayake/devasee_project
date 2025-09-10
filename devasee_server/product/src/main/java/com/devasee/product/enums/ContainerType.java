package com.devasee.product.enums;

import lombok.Getter;

@Getter
public enum ContainerType {
    BOOK("product-book-images"),
    STATIONERY("product-stationery-images"),
    PRINTING("product-printing-images"),
    PROMO("promo-images");

    private final String containerName;

    ContainerType(String containerName) {
        this.containerName = containerName;
    }

}
