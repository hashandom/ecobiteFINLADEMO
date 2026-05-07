package com.ecobite.spoilage_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "spoilage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spoilage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String spoilageCode;

    @Column(nullable = false)
    private Long batchId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private BigDecimal totalLoss;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    private SpoilageType spoilageType;

    private String reportedBy;

    private LocalDateTime reportedDate;

    @Enumerated(EnumType.STRING)
    private RecordStatus status;

}
