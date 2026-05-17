package com.ecobite.batch_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String batchNumber;

    private String productId;

    private Long supplierId;

    private int quantity;

    private int remainingQuantity;

    private LocalDate manufactureDate;

    private LocalDate expiryDate;

    private BigDecimal purchasePrice;

    private String status;

    @Column(nullable = false)
    private boolean expiryAlertSent = false;

}
