package com.engine.libs.graphics.renderEngine;

import com.engine.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class DisplaySurface {
    private ArrayList<GraphicalSurface> surfaces;
    private int width, height;
    private BufferedImage image;
    private Graphics g;
    private boolean needsUpdate, relative;

    public DisplaySurface(boolean isRelative) {
        surfaces = new ArrayList<>();
        this.relative = isRelative;
        init();
    }

    public DisplaySurface(boolean isRelative, GraphicalSurface... surfaces) {
        this.surfaces = new ArrayList<>(Arrays.asList(surfaces));
        this.relative = isRelative;
        init();
    }

    private void init() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = image.getGraphics();
        needsUpdate = false;
    }

    public void marAsUpdated() {
        needsUpdate = false;
    }

    public boolean doesNeedUpdate() {
        return needsUpdate;
    }

    public void update() {
        g.clearRect(0, 0, width, height);
        for(GraphicalSurface surface : surfaces) {
            if (surface.doesNeedUpdate()) {
                needsUpdate = true;
                g.drawImage(surface.getImage(), 0, 0, null);
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
