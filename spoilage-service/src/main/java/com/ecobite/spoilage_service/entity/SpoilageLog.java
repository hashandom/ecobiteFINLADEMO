package com.ecobite.spoilage_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class SpoilageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long batchId;

    private int quantity;

    private String reason;

    private LocalDate reportedDate;

    public Long getId() { return id; }

    public Long getBatchId() { return batchId; }

    public int getQuantity() { return quantity; }

    public String getReason() { return reason; }

    public LocalDate getReportedDate() { return reportedDate; }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }
}
