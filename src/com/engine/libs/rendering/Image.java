package com.engine.libs.rendering;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {

    private BufferedImage image;
    private int w, h;
    private int[] p;
    public File location;
    public String resLocation;

    public Image(String path) {
        try {
            image = ImageIO.read(Image.class.getResourceAsStream(path));
            location = new File(Image.class.getResource(path).getFile());
            resLocation = path;
        } catch (IOException e) {
            e.printStackTrace();
        }

        w=image.getWidth();
        h=image.getHeight();
        p=image.getRGB(0, 0, w, h, null, 0, w);
    }

    public Image(BufferedImage img) {
        image = img;

        w=image.getWidth();
        h=image.getHeight();
        p=image.getRGB(0, 0, w, h, null, 0, w);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int[] getP() {
        return p;
    }

    public void setP(int[] p) {
        this.p = p;
    }
}
