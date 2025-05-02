package org.example.meetlearning.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Base64;

public class QrCodeGenerator {

    // 生成二维码图片 Base64
    public static String generateBase64(String content, int width, int height) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(image, "PNG", os);

        return "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
    }

    // 生成二维码图片 URL（需存储到文件系统或OSS）
    public static String generateFileUrl(String content, int width, int height) throws Exception {
        String fileName = "qrcode_" + System.currentTimeMillis() + ".png";
        String savePath = "/var/www/qrcodes/" + fileName; // 存储路径

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);

        try (FileOutputStream fos = new FileOutputStream(savePath)) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", fos);
        }

        return "https://your-domain.com/qrcodes/" + fileName;
    }
}