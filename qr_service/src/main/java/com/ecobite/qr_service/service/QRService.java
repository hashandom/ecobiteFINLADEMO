package com.ecobite.qr_service.service;

import com.ecobite.qr_service.entity.QRCode;

public interface QRService {
    QRCode generateQR(Long batchId);

    QRCode getQR(String code);
}
