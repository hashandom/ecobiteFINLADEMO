package com.ecobite.qr_service.repository;

import com.ecobite.qr_service.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QrRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByQrCodeId(String qrCodeId);
    Optional<QrCode> findByBatchId(String batchId);
}
