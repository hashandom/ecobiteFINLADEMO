package com.ecobite.qr_service.repository;

import com.ecobite.qr_service.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QRRepository extends JpaRepository<QRCode, Long> {
    Optional<QRCode> findByCode(String code);
}
