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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QrServiceImpl implements QrService {
    private final QrRepository qrRepository;

    private final BatchClient batchServiceClient;

    private final ProductClient productClient;

    private final SupplierClient supplierClient;

    private final LocationClient locationClient;

    @Value("${qr.storage-path}")
    private String storagePath;

    @Value("${qr.public-url}")
    private String publicUrl;

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
    public GenerateQrResponse generateQr(
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
                    publicUrl
                            + "/qr/scan/"
                            + qrCodeId;

            String filePath =
                    storagePath
                            + "/"
                            + qrCodeId
                            + ".png";

            System.out.println("Working Directory: "
                    + System.getProperty("user.dir"));
            System.out.println("QR File Path: "
                    + filePath);

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
                    "/qr/image/" + qrCodeId
            );

            qrCode.setCreatedAt(
                    LocalDateTime.now()
            );

            qrCode.setStatus(
                    "ACTIVE"
            );

            // Save QR
            qrRepository.save(qrCode);

            GenerateQrResponse response =
                    new GenerateQrResponse();

            response.setQrCodeId(qrCode.getQrCodeId());
            response.setBatchId(qrCode.getBatchId());
            response.setQrImageUrl(
                    publicUrl + qrCode.getQrImageUrl()
            );
            response.setStatus(qrCode.getStatus());

            return response;

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
        System.out.println("Fetching Batch...");
        BatchResponse batch =
                batchServiceClient.getBatch(
                        qrCode.getBatchId()
                );
        System.out.println("Batch fetched successfully");

        // Get product details
        System.out.println("Fetching Product...");
        ProductResponse product =
                productClient.getProduct(
                        batch.getProductId()
                );
        System.out.println("Product fetched successfully");

        // Get supplier details
        System.out.println("Fetching Supplier...");
        SupplierResponse supplier =
                supplierClient.getSupplier(
                        batch.getSupplierId()
                );
        System.out.println("Supplier fetched successfully");

        // Get location details
        System.out.println("Fetching Location...");
        LocationResponse location =
                locationClient.getLocation(
                        batch.getLocationId()
                );
        System.out.println("Location fetched successfully");

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
                publicUrl
                        + qrCode.getQrImageUrl()
        );

        return response;
    }

    // Generate QR ID
    private String generateQrId() {
        return "QR" + System.currentTimeMillis();
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
                publicUrl
                        + qrCode.getQrImageUrl()
        );

        response.setStatus(
                qrCode.getStatus()
        );

        return response;
    }
}
