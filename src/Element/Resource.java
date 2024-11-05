package Element;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Resource {
    private static final Map<String, BufferedImage> images = new ConcurrentHashMap<>();

    public static BufferedImage getImage(String path) {
        return images.computeIfAbsent(path, p -> {
            try { return ImageIO.read(new File(p)); } catch (Exception e) { e.printStackTrace(); }
            return null;
        });
    }
}
