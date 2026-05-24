package com.ecobite.qr_service.service;

import com.ecobite.qr_service.dto.request.GenerateQrRequest;
import com.ecobite.qr_service.dto.response.ScanQrResponse;

public interface QrService {
    ScanQrResponse generateQr(GenerateQrRequest request);
    ScanQrResponse getQrByBatchId(String batchId);
    ScanQrResponse scanQr(String qrCodeId);
}
