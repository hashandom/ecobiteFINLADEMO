package com.ecobite.qr_service.service;

import com.ecobite.qr_service.dto.request.GenerateQrRequest;
import com.ecobite.qr_service.dto.response.*;
import com.ecobite.qr_service.entity.QrCode;
import com.ecobite.qr_service.exception.InvalidRequestException;
import com.ecobite.qr_service.exception.QrGenerationException;
import com.ecobite.qr_service.exception.ResourceNotFoundException;
import com.ecobite.qr_service.feign.BatchClient;
import com.ecobite.qr_service.feign.LocationClient;
import com.ecobite.qr_service.feign.ProductClient;
import com.ecobite.qr_service.feign.SupplierClient;
import com.ecobite.qr_service.repository.QrRepository;
import com.ecobite.qr_service.util.QrGeneratorUtil;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QrServiceImpl implements QrService {
    private final QrRepository qrRepository;

    private final BatchClient batchServiceClient;

    private final ProductClient productClient;

    private final SupplierClient supplierClient;

    private final LocationClient locationClient;

    public QrServiceImpl(
            QrRepository qrRepository,
            BatchClient batchServiceClient,
            ProductClient productClient,
            SupplierClient supplierClient,
            LocationClient locationClient
    ) {

        this.qrRepository = qrRepository;
        this.batchServiceClient = batchServiceClient;
        this.productClient = productClient;
        this.supplierClient = supplierClient;
        this.locationClient = locationClient;
    }

    @Override
    public ScanQrResponse generateQr(
            GenerateQrRequest request
    ) {

        BatchResponse batch;

        // Validate batch existence
        try {

            batch = batchServiceClient.getBatch(
                    request.getBatchId()
            );

        } catch (FeignException.BadRequest ex) {

            throw new InvalidRequestException(
                    "Invalid batch ID format"
            );

        } catch (FeignException.NotFound ex) {

            throw new ResourceNotFoundException(
                    "Batch not found"
            );

        } catch (FeignException ex) {

            throw new QrGenerationException(
                    "Batch service unavailable"
            );
        }

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
            String qrCodeId =
                    generateQrId();

            // Generate QR scan URL
            String qrData =
                    "http://localhost:8080/qr/scan/"
                            + qrCodeId;

            String filePath =
                    System.getProperty("user.dir")
                            + "/qr_service/src/main/resources/static/qrcodes/"
                            + qrCodeId
                            + ".png";

            System.out.println("QR FILE PATH: " + filePath);

            String qrImageUrl =
                    QrGeneratorUtil.generateQrImage(
                            qrData,
                            300,
                            300,
                            filePath
                    );

            // Create QR entity
            QrCode qrCode =
                    new QrCode();

            qrCode.setQrCodeId(
                    qrCodeId
            );

            qrCode.setBatchId(
                    request.getBatchId()
            );

            qrCode.setQrData(
                    qrData
            );

            qrCode.setQrImageUrl(
                    qrImageUrl
            );

            qrCode.setCreatedAt(
                    LocalDateTime.now()
            );

            qrCode.setStatus(
                    "ACTIVE"
            );

            // Save QR
            qrRepository.save(qrCode);

            return mapToResponse(qrCode);

        } catch (Exception ex) {

            ex.printStackTrace();

            throw new QrGenerationException(
                    "Failed to generate QR code"
            );
        }
    }

    @Override
    public ScanQrResponse getQrByBatchId(
            String batchId
    ) {

        QrCode qrCode =
                qrRepository.findByBatchId(
                                batchId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "QR not found for batch: "
                                                + batchId
                                )
                        );

        return mapToResponse(qrCode);
    }

    @Override
    public ScanQrResponse scanQr(
            String qrCodeId
    ) {

        // Find QR
        QrCode qrCode =
                qrRepository.findByQrCodeId(
                                qrCodeId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "QR not found"
                                )
                        );

        // Get batch details
        BatchResponse batch =
                batchServiceClient.getBatch(
                        qrCode.getBatchId()
                );

        // Get product details
        ProductResponse product =
                productClient.getProduct(
                        batch.getProductId()
                );

        // Get supplier details
        SupplierResponse supplier =
                supplierClient.getSupplier(
                        batch.getSupplierId()
                );

        // Get location details
        LocationResponse location =
                locationClient.getLocation(
                        batch.getLocationId()
                );

        // Build response
        ScanQrResponse response =
                new ScanQrResponse();

        response.setQrCodeId(
                qrCode.getQrCodeId()
        );

        response.setBatchId(
                batch.getBatchNumber()
        );

        response.setProductName(
                product.getName()
        );

        response.setSupplierName(
                supplier.getName()
        );

        response.setLocation(
                location.getLocationCode()
        );

        response.setExpiryDate(
                batch.getExpiryDate().toString()
        );

        response.setStatus(
                batch.getStatus()
        );

        response.setQuantity(
                batch.getQuantity()
        );

        response.setQrImageUrl(
                qrCode.getQrImageUrl()
        );

        return response;
    }

    // Generate QR ID
    private String generateQrId() {

        long count =
                qrRepository.count() + 1;

        return "QR"
                + String.format(
                "%03d",
                count
        );
    }

    // Entity -> DTO mapping
    private ScanQrResponse mapToResponse(
            QrCode qrCode
    ) {

        ScanQrResponse response =
                new ScanQrResponse();

        response.setQrCodeId(
                qrCode.getQrCodeId()
        );

        response.setBatchId(
                qrCode.getBatchId()
        );

        response.setQrImageUrl(
                qrCode.getQrImageUrl()
        );

        response.setStatus(
                qrCode.getStatus()
        );

        return response;
    }
}
