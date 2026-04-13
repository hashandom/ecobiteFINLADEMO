package com.ecobite.batch_service.dto;

import java.time.LocalDate;

public class BatchRequestDTO {
    private Long productId;
    private int quantity;
    private LocalDate expiryDate;

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
