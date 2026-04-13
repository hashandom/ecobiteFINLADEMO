package com.ecobite.recall_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Recall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long batchId;

    private String reason;

    private String status;

    private LocalDate recallDate;

    public Long getId() { return id; }

    public Long getBatchId() { return batchId; }

    public String getReason() { return reason; }

    public String getStatus() { return status; }

    public LocalDate getRecallDate() { return recallDate; }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRecallDate(LocalDate recallDate) {
        this.recallDate = recallDate;
    }

}
