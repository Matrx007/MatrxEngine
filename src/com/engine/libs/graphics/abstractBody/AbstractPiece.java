package com.engine.libs.graphics.abstractBody;

import com.engine.libs.rendering.Renderer;

import java.awt.*;

public abstract class AbstractPiece {
    protected double x, y, w, h;
    protected Color color;

    public AbstractPiece(double x, double y, double w, double h, Color color) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
    }

    public abstract void render(Renderer r, int x, int y, int width, int height);
}
