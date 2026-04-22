package com.ecobite.batch_service.dto.Kafkaevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchEvent {
    private String eventType; // EXPIRING_SOON
    private String productName;
    private Long batchId;
    private LocalDate expiryDate;
    private int remainingQuantity;
}
