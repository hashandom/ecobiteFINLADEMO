package com.ecobite.qr_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String qrCodeId;

    private String batchId;

    private String qrData;

    private String qrImageUrl;

    private LocalDateTime createdAt;

    private String status;

}
