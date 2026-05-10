package com.ecobite.qr_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
@Data
@NoArgsConstructor
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
