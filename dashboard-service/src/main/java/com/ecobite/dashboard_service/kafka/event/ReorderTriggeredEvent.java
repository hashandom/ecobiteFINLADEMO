package com.ecobite.dashboard_service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReorderTriggeredEvent {
    private Long productId;
    private String productName;
    private Integer reorderQuantity;
}
