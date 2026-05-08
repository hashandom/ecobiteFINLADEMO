package com.ecobite.recall_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_recalls")
public class ProductRecall {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID recallId;

    @Column(nullable = false, unique = true)
    private String recallCode;

    @Column(nullable = false)
    private String batchId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    private RecallStatus status;

    private LocalDateTime recalledAt;

    private String initiatedBy;


}
