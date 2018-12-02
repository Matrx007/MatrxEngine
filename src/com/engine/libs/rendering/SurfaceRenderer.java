package com.engine.libs.rendering;

import com.engine.Core;
import com.engine.libs.graphics.renderEngine.DisplaySurface;

import java.awt.*;
import java.util.ArrayList;

public class SurfaceRenderer {
    private Core core;
    private Graphics2D g2;
    private int w, h, viewX, viewY;

    public SurfaceRenderer(Core core) {
        this.core = core;
        this.g2 = (Graphics2D)core.getWindow().getG();
        w = core.getWidth();
        h = core.getHeight();
        viewX = 0;
        viewY = 0;
    }

    public Graphics2D getG() {
        return g2;
    }

    private ArrayList<DisplaySurface> displaySurfaces;

    public void addDisplaySurface(DisplaySurface displaySurface) {
        displaySurfaces.add(displaySurface);
    }

    public void render() {
        boolean needsUpdate = false;
        for(DisplaySurface displaySurface : displaySurfaces) needsUpdate = displaySurface.doesNeedUpdate() || needsUpdate;
        if(needsUpdate) g2.clearRect(0, 0, w, h);
        for(DisplaySurface displaySurface : displaySurfaces) g2.drawImage(displaySurface.getImage(), 0, 0, null);
    }
}
