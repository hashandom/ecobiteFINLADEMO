package com.ecobite.qr_service.service;

import com.ecobite.qr_service.dto.request.GenerateQrRequest;
import com.ecobite.qr_service.dto.response.GenerateQrResponse;
import com.ecobite.qr_service.dto.response.QrListResponse;
import com.ecobite.qr_service.dto.response.ScanQrResponse;

import java.util.List;

public interface QrService {
    GenerateQrResponse generateQr(GenerateQrRequest request);
    ScanQrResponse getQrByBatchId(String batchId);
    ScanQrResponse scanQr(String qrCodeId);
    List<QrListResponse> getAllQrCodes();
}
