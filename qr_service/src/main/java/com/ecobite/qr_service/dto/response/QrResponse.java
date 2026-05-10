package com.ecobite.qr_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QrResponse {
    private String qrCodeId;
    private String batchId;
    private String qrImageUrl;
    private String message;
}
