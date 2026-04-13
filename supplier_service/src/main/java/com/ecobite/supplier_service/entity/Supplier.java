package com.ecobite.supplier_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double qualityScore;

    private double deliveryScore;

    private double costScore;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQualityScore() {
        return qualityScore;
    }

    public double getDeliveryScore() {
        return deliveryScore;
    }

    public double getCostScore() {
        return costScore;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQualityScore(double qualityScore) {
        this.qualityScore = qualityScore;
    }

    public void setDeliveryScore(double deliveryScore) {
        this.deliveryScore = deliveryScore;
    }

    public void setCostScore(double costScore) {
        this.costScore = costScore;
    }
}
