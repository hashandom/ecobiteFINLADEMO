package com.ecobite.spoilage_service.dto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;

@Getter
@Setter
public class BatchResponseDTO {
    private Long id;
    private String batchCode;
    private Integer availableQuantity;
    private String status;
}
