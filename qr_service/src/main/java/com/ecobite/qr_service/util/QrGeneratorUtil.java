package com.ecobite.qr_service.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QrGeneratorUtil {
    public static String generateQrImage(
            String text,
            int width,
            int height,
            String filePath
    ) throws Exception {

        Map<EncodeHintType, Object> hints =
                new HashMap<>();

        hints.put(
                EncodeHintType.CHARACTER_SET,
                "UTF-8"
        );

        BitMatrix matrix =
                new MultiFormatWriter().encode(
                        text,
                        BarcodeFormat.QR_CODE,
                        width,
                        height,
                        hints
                );

        Path path = FileSystems.getDefault()
                .getPath(filePath);

        // Create directories automatically
        Files.createDirectories(
                path.getParent()
        );

        MatrixToImageWriter.writeToPath(
                matrix,
                "PNG",
                path
        );

        return filePath;
    }
}
