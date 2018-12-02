package com.engine.libs.graphics.renderEngine;

import javafx.scene.shape.DrawMode;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Source {
    private BufferedImage image;
    private boolean needsUpdate, hasAlphaBackground;
    private int x, y, alpha;
    private Drawable drawable;

    public Source(BufferedImage image, boolean hasAlphaBackground) {
        this.image = image;
        needsUpdate = true;
        init(hasAlphaBackground);
    }
    public Source(BufferedImage image, boolean hasAlphaBackground, int x, int y) {
        this.image = image;
        needsUpdate = true;
        this.x = x;
        this.y = y;
        init(hasAlphaBackground);
    }
    public Source(BufferedImage image, boolean hasAlphaBackground, int alpha) {
        this.image = image;
        needsUpdate = true;
        this.alpha = alpha;
        init(hasAlphaBackground);
    }
    public Source(BufferedImage image, boolean hasAlphaBackground, int x, int y, int alpha) {
        this.image = image;
        needsUpdate = true;
        this.x = x;
        this.y = y;
        this.alpha = alpha;
        init(hasAlphaBackground);
    }
    public Source(Drawable drawable, boolean hasAlphaBackground) {
        this.drawable = drawable;
        needsUpdate = true;
        init(hasAlphaBackground);
    }
    public Source(Drawable drawable, boolean hasAlphaBackground, int x, int y) {
        this.drawable = drawable;
        needsUpdate = true;
        this.x = x;
        this.y = y;
        init(hasAlphaBackground);
    }
    public Source(Drawable drawable, boolean hasAlphaBackground, int alpha) {
        this.drawable = drawable;
        needsUpdate = true;
        this.alpha = alpha;
        init(hasAlphaBackground);
    }
    public Source(Drawable drawable, boolean hasAlphaBackground, int x, int y, int alpha) {
        this.drawable = drawable;
        needsUpdate = true;
        this.x = x;
        this.y = y;
        this.alpha = alpha;
        init(hasAlphaBackground);
    }

    private void init(boolean hasAlphaBackground) {
        this.hasAlphaBackground = hasAlphaBackground;
    }

    public void render(Graphics2D g2) {
        if(drawable == null) {
            g2.drawImage(image, x, y, null);
        } else {
            drawable.render(g2);
        }
    }

    public void refresh(BufferedImage image) {
        this.image = image;
        needsUpdate = false;
    }

    void markAsUpdated() {
        needsUpdate = false;
    }

    public void requestUpdate() {
        needsUpdate = true;
    }

    public boolean asksUpdate() {
        return needsUpdate;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        needsUpdate = true;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        needsUpdate = true;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        needsUpdate = true;
    }

    public boolean hasAlphaBackground() {
        return hasAlphaBackground;
    }

    public void setAlphaBackground(boolean hasAlphaBackground) {
        this.hasAlphaBackground = hasAlphaBackground;
        needsUpdate = true;
    }
}
