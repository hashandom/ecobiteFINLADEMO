package com.ecobite.batch_service.dto;

import java.time.LocalDate;

public class BatchResponseDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private LocalDate expiryDate;

    public BatchResponseDTO() {}

    public BatchResponseDTO(Long id, Long productId, int quantity, LocalDate expiryDate) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
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
