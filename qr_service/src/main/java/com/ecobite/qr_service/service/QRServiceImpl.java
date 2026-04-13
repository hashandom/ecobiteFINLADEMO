package com.ecobite.qr_service.service;

import com.ecobite.qr_service.entity.QRCode;
import com.ecobite.qr_service.repository.QRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QRServiceImpl implements QRService{
    @Autowired
    private QRRepository repository;

    @Override
    public QRCode generateQR(Long batchId) {

        QRCode qr = new QRCode();
        qr.setBatchId(batchId);
        qr.setCode(UUID.randomUUID().toString());

        return repository.save(qr);
    }

    @Override
    public QRCode getQR(String code) {

        return repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("QR not found"));
    }
}
