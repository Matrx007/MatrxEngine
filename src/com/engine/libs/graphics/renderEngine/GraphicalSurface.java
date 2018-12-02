package com.engine.libs.graphics.renderEngine;

import com.engine.Core;
import com.engine.Game;
import com.engine.libs.rendering.SurfaceRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class GraphicalSurface {
    private ArrayList<Source> sources;
    private Graphics2D g2;
    private BufferedImage image;
    private boolean hasAlpha;
    private int width=-1, height=-1;
    private boolean needsUpdate;

    public void setDimension(Dimension dimension) {
        this.width = dimension.width;
        this.height = dimension.height;
        init();
    }
    public void setDimension(int w, int h) {
        this.width = w;
        this.height = h;
        init();
    }
    public void setDimension(DisplaySurface displaySurface) {
        this.width = displaySurface.getWidth();
        this.height = displaySurface.getHeight();
        init();
    }
    public void setDimension(Core core) {
        width = core.getWidth();
        height = core.getHeight();
        init();
    }
    public void setDimension(Game game) {
        width = game.getE().getWidth();
        height = game.getE().getHeight();
        init();
    }

    public GraphicalSurface(SurfaceRenderer surfaceRenderer, Source... sources) {
        this.sources = new ArrayList<>(Arrays.asList(sources));
        g2 = surfaceRenderer.getG();
        cleanUpdate();
    }

    private void init() {
        needsUpdate = true;
        if(width < 1) {
            try {
                throw new InvalidDimensionsException(width);
            } catch (InvalidDimensionsException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        } // Avoid invalid dimensions
        if(height < 1) {
            try {
                throw new InvalidDimensionsException(height);
            } catch (InvalidDimensionsException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        } // Avoid invalid dimensions

        hasAlpha = false;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void markAsUpdated() {
        needsUpdate = false;
    }

    public boolean doesNeedUpdate() {
        return needsUpdate;
    }

    public GraphicalSurface() {
        sources = new ArrayList<>();
    }

    public void addSource(Source source) {
        hasAlpha = hasAlpha || source.hasAlphaBackground();
        this.sources.add(source);
    }

    public void cleanUpdate() {
        g2.clearRect(0, 0, width, height);
        hasAlpha = false;
        for(Source source : sources) {
            source.render(g2);
            hasAlpha = hasAlpha || source.hasAlphaBackground();
        }
    }

    public void update() {
        for(Source source : sources) {
            if(source.asksUpdate()) {
                needsUpdate = true;
                hasAlpha = hasAlpha || source.hasAlphaBackground();
                if(source.hasAlphaBackground()) {
                    g2.clearRect(source.getX(), source.getY(), source.getWidth(), source.getHeight());
                }
                source.render(g2);
                source.markAsUpdated();
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
