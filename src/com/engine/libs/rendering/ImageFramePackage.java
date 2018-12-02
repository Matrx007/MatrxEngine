package com.engine.libs.rendering;

import java.awt.image.BufferedImage;

public class ImageFramePackage {
    private BufferedImage image;
    private int w, h;
    private int[] p;
    private int subimage = 0;
    private BufferedImage subImg;
    private BufferedImage[] images;
    private int spriteW;
    private int subimages;
    private int currentTime;
    private int maxTime;

    public ImageFramePackage(Image img, int spriteWidth) {
        image = img.getImage();
        spriteW = spriteWidth;

        subImg = image.getSubimage(0, 0, spriteW, image.getHeight());

        w=image.getWidth();
        h=image.getHeight();
        p=image.getRGB(0, 0, w, h, null, 0, w);
        images = new BufferedImage[image.getWidth()/spriteWidth];
        for(int i = 0; i < images.length; i++) {
            images[i] = fetchImage(i);
        }
    }

    private BufferedImage getImage(int index) {
        return images[index];
    }

    private BufferedImage fetchImage(int subimage) {
        return subImg = image.getSubimage(subimage*spriteW, 0, spriteW, image.getHeight());
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

    public BufferedImage[] getImages() {
        return images;
    }
}
