package com.ecobite.batch_service.dto;

import java.time.LocalDate;

public class BatchResponseDTO {
    private Long id;
    private String productId;
    private int quantity;
    private LocalDate expiryDate;

    public BatchResponseDTO() {}

    public BatchResponseDTO(Long id, String productId, int quantity, LocalDate expiryDate) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
