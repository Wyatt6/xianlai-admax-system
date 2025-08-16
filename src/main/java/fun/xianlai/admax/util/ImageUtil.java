package fun.xianlai.admax.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author WyattLau
 * @date 2024/2/2
 */
public class ImageUtil {
    public static String bufferedImageToBase64(BufferedImage image, String imageType) throws IOException {
        if (imageType == null || imageType.isEmpty()) {
            imageType = "png";
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, imageType, stream);
        return "data:image/" + imageType + ";base64," + Base64.getEncoder().encodeToString(stream.toByteArray());
    }
}
