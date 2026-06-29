package com.ecobite.qr_service.dto.response;

import lombok.Data;

@Data
public class GenerateQrResponse {
    private String qrCodeId;
    private String batchId;
    private String qrImageUrl;
    private String status;
}
