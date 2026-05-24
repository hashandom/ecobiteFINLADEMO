package com.ecobite.qr_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScanQrResponse {
    private String qrCodeId;

    private String batchId;

    private String productName;

    private String supplierName;

    private String location;

    private String expiryDate;

    private String status;

    private Integer quantity;

    private String qrImageUrl;

}
