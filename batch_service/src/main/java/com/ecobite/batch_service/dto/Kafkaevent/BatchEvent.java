package com.ecobite.batch_service.dto.Kafkaevent;

import lombok.Data;

@Data
public class BatchEvent {
    private String eventType; // EXPIRING_SOON
    private String productName;
    private String batchId;
    private String expiryDate;
}
