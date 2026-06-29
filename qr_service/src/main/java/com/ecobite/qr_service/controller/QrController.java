package com.ecobite.qr_service.controller;

import com.ecobite.qr_service.dto.request.GenerateQrRequest;
import com.ecobite.qr_service.dto.response.ApiResponse;
import com.ecobite.qr_service.dto.response.GenerateQrResponse;
import com.ecobite.qr_service.dto.response.QrListResponse;
import com.ecobite.qr_service.dto.response.ScanQrResponse;
import com.ecobite.qr_service.service.QrService;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/qr")

public class QrController {
    @Autowired
    private QrService qrService;

    @Value("${qr.storage-path}")
    private String storagePath;

    /*
     * Generate QR Code
     * Requires authentication
     */
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<GenerateQrResponse>> generateQr(
            @Valid @RequestBody GenerateQrRequest request
    ) {

        GenerateQrResponse response =
                qrService.generateQr(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                201,
                                "QR generated successfully",
                                response,
                                LocalDateTime.now()
                        )
                );
    }

    /*
     * Scan QR Code
     * Public endpoint
     */
    @GetMapping("/scan/{qrCodeId}")
    public ResponseEntity<ApiResponse<?>> scanQr(
            @PathVariable String qrCodeId
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "QR scanned successfully",
                        qrService.scanQr(qrCodeId),
                        LocalDateTime.now()
                )
        );
    }

    /*
     * Retrieve QR Image
     * Public endpoint
     */
    @GetMapping(
            value = "/image/{qrCodeId}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<Resource> getQrImage(
            @PathVariable String qrCodeId
    ) throws Exception {

        Path path = Paths.get(
                storagePath,
                qrCodeId + ".png"
        );

        Resource resource =
                new UrlResource(
                        path.toUri()
                );

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(
                        MediaType.IMAGE_PNG
                )
                .body(resource);
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<ApiResponse<ScanQrResponse>> getQrByBatchId(
            @PathVariable String batchId
    ) {

        ScanQrResponse response =
                qrService.getQrByBatchId(batchId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "QR retrieved successfully",
                        response,
                        LocalDateTime.now()
                )
        );
    }

    @GetMapping("/all-QRImages")
    public ResponseEntity<ApiResponse<List<QrListResponse>>> getAllQrCodes() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "QR codes retrieved successfully",
                        qrService.getAllQrCodes(),
                        LocalDateTime.now()
                )
        );
    }
}
