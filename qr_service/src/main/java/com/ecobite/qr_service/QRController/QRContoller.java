package com.ecobite.qr_service.QRController;

import com.ecobite.qr_service.entity.QRCode;
import com.ecobite.qr_service.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qr")
public class QRContoller {
    @Autowired
    private QRService qrService;

    @PostMapping("/generate/{batchId}")
    public QRCode generateQR(@PathVariable Long batchId) {
        return qrService.generateQR(batchId);
    }

    @GetMapping("/{code}")
    public QRCode getQR(@PathVariable String code) {
        return qrService.getQR(code);
    }
}
