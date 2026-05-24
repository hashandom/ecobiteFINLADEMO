package com.ecobite.qr_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class GenerateQrRequest {
    @NotBlank(
            message = "Batch ID is required"
    )

    @Pattern(
            regexp = "^[0-9]+$",
            message = "Batch ID must be numeric"
    )
    private String batchId;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(
            String batchId
    ) {
        this.batchId = batchId;
    }
}
