package com.engine.libs.rendering;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RenderUtils {
    private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static GraphicsDevice gs = ge.getDefaultScreenDevice();
    private static GraphicsConfiguration gc = gs.getDefaultConfiguration();

    public static BufferedImage createImage(int width, int height, boolean opaque) {
        return gc.createCompatibleImage(width, height, opaque ? Transparency.OPAQUE : Transparency.TRANSLUCENT);
    }

    public static BufferedImage createImage(int width, int height) {
        return gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    public static BufferedImage makeCompatible(BufferedImage image)  {
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

        if (image.getColorModel().equals(gfxConfig.getColorModel()))
            return image;

        BufferedImage newImage = gfxConfig.createCompatibleImage(
                image.getWidth(), image.getHeight(), image.getTransparency());

        Graphics2D g2d = newImage.createGraphics();

        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return newImage;
    }

    public static BufferedImage resizeWithCompatibleImage(BufferedImage img, int width, int height) {
        if(img == null) return null;
        BufferedImage newImage = createImage(width, height);
        newImage.getGraphics().drawImage(img, 0, 0, width, height, null);
        return newImage;
    }

    public static BufferedImage resize(BufferedImage img, int width, int height) {
        if(img == null) return null;
        BufferedImage newImage = new BufferedImage(width, height, img.getType());
        newImage.getGraphics().drawImage(img, 0, 0, width, height, null);
        return newImage;
    }

    public static String imageToBase64(BufferedImage image) {
        if(image == null) return null;
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static BufferedImage base64ToImage(String imageString) {
        if(imageString == null) return null;

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage invert(BufferedImage image) {
        if(image == null) return null;
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgba = image.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue(),
                        col.getAlpha());
                newImage.setRGB(x, y, col.getRGB());
            }
        }
        return newImage;
    }

    public static BufferedImage colorize(BufferedImage image, Color color) {
        if(image == null) return null;
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgba = image.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(
                        (int)((double)col.getRed()/255d*(double)color.getRed()),
                        (int)((double)col.getGreen()/255d*(double)color.getGreen()),
                        (int)((double)col.getBlue()/255d*(double)color.getBlue()),
                        col.getAlpha()
                );
                newImage.setRGB(x, y, col.getRGB());
            }
        }
        return newImage;
    }
}
