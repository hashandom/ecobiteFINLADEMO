package com.ecobite.product_service.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="products")
@Entity
public class Product {
    @Id
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getStock() {
        return stock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    @Column(nullable=false)
    private String name;

    private String category;

    private int stock;

    private int reorderLevel;

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    private double unitPrice;

    }

