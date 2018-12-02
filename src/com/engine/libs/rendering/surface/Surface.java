package com.engine.libs.rendering.surface;

import com.engine.libs.rendering.Renderer;

import java.awt.image.BufferedImage;

public class Surface {
    private int x, y, w, h, newW, newH;
    private BufferedImage surface;

    public Surface(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.surface = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    public Surface(int x, int y, int w, int h, int newW, int newH) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.newW = newW;
        this.newH = newH;
        this.surface = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    public void render(Renderer r) {
        if(newW == 0 || newH == 0) {
            r.drawImage(x, y, surface);
        } else {
            r.drawImage(x, y, newW, newH, surface);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getNewW() {
        return newW;
    }

    public int getNewH() {
        return newH;
    }

    public BufferedImage getSurface() {
        return surface;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setNewW(int newW) {
        this.newW = newW;
    }

    public void setNewH(int newH) {
        this.newH = newH;
    }
}
