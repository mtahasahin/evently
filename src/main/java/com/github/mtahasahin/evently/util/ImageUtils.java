package com.github.mtahasahin.evently.util;

import com.github.mtahasahin.evently.exception.CustomValidationException;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ImageUtils {

    private static final List<String> allowedMimeTypes = new ArrayList<>(List.of("image/jpeg", "image/png"));

    public static final HashMap<String, String> extensions = new HashMap<>() {{
        put("image/png", ".png");
        put("image/jpeg", ".jpeg");
    }};

    public static ApiResponse<Boolean> isValid(MultipartFile image, int maxSize, int minHeight, int minWidth) {
        var tika = new Tika();
        if (image == null) {
            throw new CustomValidationException(new ApiResponse.ApiSubError("image", "Image is required"));
        }
        try {
            var mimeType = tika.detect(image.getBytes());
            if(!allowedMimeTypes.contains(mimeType)) {
                return ApiResponse.Error(false,"Invalid image type");
            }
            if(image.getSize() > maxSize) {
                return ApiResponse.Error(false,"Image size is too big");
            }
            var bufferedImage = ImageIO.read(image.getInputStream());
            if(bufferedImage.getHeight() < minHeight || bufferedImage.getWidth() < minWidth) {
                return ApiResponse.Error(false,"Image size is too small");
            }
        } catch (IOException e) {
            return ApiResponse.Error(false,"Invalid image");
        }
        return ApiResponse.Success(true);
    }

    public static String getExtension(MultipartFile image) {
        var tika = new Tika();
        try {
            return extensions.get(tika.detect(image.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid image");
        }
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    public static BufferedImage createProfileImageFromName(String name, int size) {
        String initials = "";
        if(name.contains(" ")) {
            var firstLetter = name.substring(0,1).toUpperCase(Locale.ROOT);
            var secondLetter = name.substring(name.indexOf(" ") + 1, name.indexOf(" ") + 2).toUpperCase();
            initials = firstLetter + secondLetter;
        }
        else {
            initials = name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1, 2);
        }

        int imgWidth = 250;
        int imgHeight = 250;
        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = img.createGraphics();

        Color backgroundColor = new Color(255, 183, 0);
        g.setPaint(backgroundColor);
        g.fillRect(0, 0, imgWidth, imgHeight);

        Font font = new Font("Arial", Font.BOLD, 100);
        g.setFont(font);
        g.setPaint(Color.WHITE);

        TextLayout textLayout = new TextLayout(initials, g.getFont(),
                g.getFontRenderContext());
        double textHeight = textLayout.getBounds().getHeight();
        double textWidth = textLayout.getBounds().getWidth();

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.drawString(initials, imgWidth / 2 - (int) textWidth / 2,
                imgHeight / 2 + (int) textHeight / 2);

        g.dispose();
        return img;
    }

}
