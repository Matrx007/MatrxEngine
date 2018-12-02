package com.engine.libs.rendering;

public class ImageAnimation {
    private Image img, sprite=null;
    private int currentX, currentY, w, h;
    private double tick, speed;
    public boolean isAnimation;

    public ImageAnimation(Image img, int w, int h, double speed) {
        this.img = img;
        this.w = w;
        this.h = h;
        this.currentX = 0;
        this.currentY = 0;
        this.tick = 0;
        this.speed = speed;
        this.isAnimation = true;
    }

    public ImageAnimation(Image img) {
        this.img = img;
        this.isAnimation = false;
    }

    public void update() {
        if(isAnimation) {
            tick+=speed;
            if (tick >= 1) {
                tick = 0;
                if (currentX >= img.getW() - w) {
                    currentY += h;
                    currentX = 0;
                }
                if (currentY >= img.getH()) {
                    currentY = 0;
                    currentX = 0;
                }

                sprite = new Image(img.getImage().getSubimage(currentX, currentY, w, h));

                currentX += w;
            }
        }
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public double getTick() {
        return tick;
    }

    public void setTick(double tick) {
        this.tick = tick;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
