package com.ecobite.qr_service.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

public class QRGenerator {
    public static BufferedImage generateQR(String text) throws WriterException {

        QRCodeWriter writer = new QRCodeWriter();
        var bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 300, 300);

        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }

        return image;
    }
}
