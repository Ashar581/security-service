package com.security.service.Util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Component
public class ImageCompressionUtil {

    public byte[] compressImage(MultipartFile file) throws IOException {
        // Convert MultipartFile to BufferedImage
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        // Prepare to compress the image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);

        System.out.println(file.getContentType());
        String type = file.getContentType();
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(type.substring(type.indexOf("/")+1));
        if (!writers.hasNext()) {
            throw new IllegalStateException("No JPEG writers found.");
        }

        ImageWriter writer = writers.next();
        writer.setOutput(ios);
        ImageWriteParam param = writer.getDefaultWriteParam();

        // Set compression quality (0.0f = lowest quality, 1.0f = highest quality)
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.5f);

        // Write the image with the specified quality
        writer.write(null, new javax.imageio.IIOImage(originalImage, null, null), param);
        writer.dispose();
        ios.close();

        // Step 3: Convert ByteArrayOutputStream to byte array
        return baos.toByteArray();
    }
}

