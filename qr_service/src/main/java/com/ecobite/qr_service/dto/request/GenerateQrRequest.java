package com.ecobite.qr_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateQrRequest {
    @NotBlank(message = "Batch ID is required")
    private String batchId;
}
