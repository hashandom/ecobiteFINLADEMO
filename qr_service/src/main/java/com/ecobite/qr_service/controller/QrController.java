package com.ecobite.qr_service.controller;

import com.ecobite.qr_service.dto.request.GenerateQrRequest;
import com.ecobite.qr_service.dto.response.ApiResponse;
import com.ecobite.qr_service.dto.response.ScanQrResponse;
import com.ecobite.qr_service.service.QrService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/qr")

public class QrController {
    @Autowired
    private QrService qrService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<ScanQrResponse>>
    generateQr(
            @Valid @RequestBody GenerateQrRequest request
    ) {

        ScanQrResponse response =
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

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/scan/{qrCodeId}")
    public ResponseEntity<ApiResponse<?>>
    scanQr(
            @PathVariable String qrCodeId
    ){

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "QR scanned successfully",
                        qrService.scanQr(qrCodeId),
                        LocalDateTime.now()
                )
        );
    }
}
