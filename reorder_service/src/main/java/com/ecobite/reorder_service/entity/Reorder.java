package com.ecobite.reorder_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Reorder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long supplierId;

    private int quantity;

    private String status;

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
