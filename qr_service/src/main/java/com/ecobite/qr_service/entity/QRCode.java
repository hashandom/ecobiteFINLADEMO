package com.ecobite.qr_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class QRCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long batchId;

    private String code;

    public Long getId() { return id; }

    public Long getBatchId() { return batchId; }

    public String getCode() { return code; }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
