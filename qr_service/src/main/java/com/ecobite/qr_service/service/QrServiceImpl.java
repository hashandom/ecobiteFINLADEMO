package com.ecobite.qr_service.service;

import com.ecobite.qr_service.dto.request.GenerateQrRequest;
import com.ecobite.qr_service.dto.response.BatchResponse;
import com.ecobite.qr_service.dto.response.QrResponse;
import com.ecobite.qr_service.entity.QrCode;
import com.ecobite.qr_service.exception.InvalidRequestException;
import com.ecobite.qr_service.exception.QrGenerationException;
import com.ecobite.qr_service.exception.ResourceNotFoundException;
import com.ecobite.qr_service.feign.BatchClient;
import com.ecobite.qr_service.repository.QrRepository;
import com.ecobite.qr_service.util.QrGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QrServiceImpl implements QrService {
    @Autowired
    private QrRepository qrRepository;

    @Autowired
    private BatchClient batchServiceClient;

    @Override
    public QrResponse generateQr(GenerateQrRequest request) {

        // Validate batch existence
        BatchResponse batch =
                batchServiceClient.getBatch(
                        request.getBatchId()
                );

        if (batch == null) {

            throw new ResourceNotFoundException(
                    "Batch not found"
            );
        }

        // Prevent duplicate QR
        qrRepository.findByBatchId(
                request.getBatchId()
        ).ifPresent(qr -> {

            throw new InvalidRequestException(
                    "QR already exists for batch"
            );
        });

        try {

            // Generate QR ID
            String qrCodeId = generateQrId();

            // QR data
            String qrData =
                    "batchId=" + request.getBatchId();

            // Generate QR image
            String qrImageUrl =
                    QrGeneratorUtil.generateQrImage(
                            qrData,
                            300,
                            300,
                            System.getProperty("user.dir")
                                    + "/qr_service/src/main/resources/static/qrcodes/"
                                    + qrCodeId + ".png"
                    );

            // Create entity
            QrCode qrCode = new QrCode();

            qrCode.setQrCodeId(qrCodeId);
            qrCode.setBatchId(request.getBatchId());
            qrCode.setQrData(qrData);
            qrCode.setQrImageUrl(qrImageUrl);
            qrCode.setCreatedAt(LocalDateTime.now());
            qrCode.setStatus("ACTIVE");

            // Save DB
            qrRepository.save(qrCode);

            // Return response
            return mapToResponse(qrCode);

        } catch (Exception e) {

            throw new QrGenerationException(
                    "Failed to generate QR code"
            );
        }
    }

    @Override
    public QrResponse getQrByBatchId(String batchId) {

        QrCode qrCode = qrRepository
                .findByBatchId(batchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "QR not found for batch: "
                                        + batchId
                        )
                );

        return mapToResponse(qrCode);
    }

    @Override
    public QrResponse scanQr(String qrCodeId) {
        QrCode qrCode = qrRepository
                .findByQrCodeId(qrCodeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "QR code not found: "
                                        + qrCodeId
                        )
                );

        return mapToResponse(qrCode);
    }

    // Generate QR ID
    private String generateQrId() {
        long count = qrRepository.count() + 1;
        return "QR" + String.format("%03d", count);
    }

    // Convert Entity -> Response DTO
    private QrResponse mapToResponse(QrCode qrCode) {
        QrResponse response = new QrResponse();
        response.setQrCodeId(
                qrCode.getQrCodeId()
        );
        response.setBatchId(
                qrCode.getBatchId()
        );
        response.setQrImageUrl(
                qrCode.getQrImageUrl()
        );
        return response;
    }
}
