package com.ecobite.qr_service.service;

import com.ecobite.qr_service.dto.request.GenerateQrRequest;
import com.ecobite.qr_service.dto.response.QrResponse;
import com.ecobite.qr_service.entity.QrCode;
import com.ecobite.qr_service.exception.InvalidRequestException;
import com.ecobite.qr_service.exception.QrGenerationException;
import com.ecobite.qr_service.exception.ResourceNotFoundException;
import com.ecobite.qr_service.repository.QrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QrServiceImpl implements QrService {
    @Autowired
    private QrRepository qrRepository;

    @Autowired
    private BatchServiceClient batchServiceClient;

    @Override
    public QrResponse generateQr(GenerateQrRequest request) {

        // Validate batch existence
        BatchResponse batch =
                batchServiceClient.getBatch(request.getBatchId());

        if(batch == null){
            throw new ResourceNotFoundException(
                    "Batch not found");
        }

        // Prevent duplicate QR
        qrRepository.findByBatchId(request.getBatchId())
                .ifPresent(qr -> {
                    throw new InvalidRequestException(
                            "QR already exists for batch");
                });

        try {

            String qrCodeId = generateQrId();

            String qrData =
                    "batchId=" + request.getBatchId();

            String qrImageUrl =
                    QrGeneratorUtil.generate(qrData);

            QrCode qrCode = new QrCode();

            qrCode.setQrCodeId(qrCodeId);
            qrCode.setBatchId(request.getBatchId());
            qrCode.setQrData(qrData);
            qrCode.setQrImageUrl(qrImageUrl);
            qrCode.setCreatedAt(LocalDateTime.now());
            qrCode.setStatus("ACTIVE");

            qrRepository.save(qrCode);

            return mapToResponse(qrCode);

        } catch (Exception e) {

            throw new QrGenerationException(
                    "Failed to generate QR code");
        }
    }
}
