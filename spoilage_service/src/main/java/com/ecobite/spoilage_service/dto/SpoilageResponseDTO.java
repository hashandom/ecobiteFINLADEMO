package com.ecobite.spoilage_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SpoilageResponseDTO {
    private String spoilageCode;
    private Long batchId;
    private String productName;
    private Integer quantity;
    private BigDecimal totalLoss;
    private String reason;
    private String spoilageType;
    private LocalDateTime reportedDate;
}
