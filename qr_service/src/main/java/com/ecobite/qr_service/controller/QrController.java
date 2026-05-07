package com.ecobite.qr_service.controller;

import com.ecobite.qr_service.dto.request.GenerateQrRequest;
import com.ecobite.qr_service.dto.response.ApiResponse;
import com.ecobite.qr_service.dto.response.QrResponse;
import com.ecobite.qr_service.service.QrService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/qr")

public class QrController {
    @Autowired
    private QrService qrService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<QrResponse>>
    generateQr(
            @Valid @RequestBody GenerateQrRequest request
    ) {

        QrResponse response =
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
}
