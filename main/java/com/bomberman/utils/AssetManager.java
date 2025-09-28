package com.bomberman.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();
    private static BufferedImage placeholderImage;

    static {
        placeholderImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                placeholderImage.setRGB(i, j, 0xFF808080);
            }
        }
    }

    public static BufferedImage loadImage(String filename) {
        return imageCache.computeIfAbsent(filename, f -> {
            try {
                File file = new File("src/main/resources/images/" + f);
                if (file.exists()) {
                    return ImageIO.read(file);
                }
                return placeholderImage;
            } catch (IOException e) {
                e.printStackTrace();
                return placeholderImage;
            }
        });
    }
}