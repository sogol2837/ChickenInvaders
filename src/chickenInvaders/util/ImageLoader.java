package chickenInvaders.util;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {

    private static final Map<String, Image> cache = new HashMap<>();

    private ImageLoader() {
    }

    public static Image load(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        Image image = null;

        try (InputStream input = ImageLoader.class.getClassLoader().getResourceAsStream(path)) {
            if (input != null) {
                image = ImageIO.read(input);
            }
        } catch (IOException e) {
            System.err.println("could not load image from classpath: " + path);
        }

        if (image == null) {
            try {
                image = ImageIO.read(new File(path));
            } catch (IOException e) {
                System.err.println("could not load image file: " + path);
            }
        }

        cache.put(path, image);
        return image;
    }
}
