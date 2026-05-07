package com.ecobite.qr_service.service;

import com.ecobite.qr_service.dto.request.GenerateQrRequest;
import com.ecobite.qr_service.dto.response.QrResponse;

public interface QrService {
    QrResponse generateQr(GenerateQrRequest request);
    QrResponse getQrByBatchId(String batchId);
    QrResponse scanQr(String qrCodeId);
}
