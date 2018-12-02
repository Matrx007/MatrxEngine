package com.engine.libs.graphics.abstractBody.shapes;

import com.engine.libs.graphics.abstractBody.AbstractPiece;
import com.engine.libs.rendering.Renderer;

import java.awt.*;

public class Rectangle extends AbstractPiece {
    private boolean filled;

    public Rectangle(double x, double y, double w, double h, boolean filled, Color color) {
        super(x, y, w, h, color);
        this.filled = filled;
    }

    @Override
    public void render(Renderer r, int x, int y, int width, int height) {
        int w, h;
        if(this.w == -1.0) {
            w = (int)(this.h*height);
            h = w;
        } else if(this.h == -1.0) {
            h = (int)(this.w*width);
            w = h;
        } else {
            w = (int)(this.w*width);
            h = (int)(this.h*height);
        }
        int xx, yy;
        if(this.x == -1.0d) {
            yy = (int)(this.y*width);
            xx = yy;
        } else if(this.y == -1.0d) {
            xx = (int)(this.x*height);
            yy = xx;
        } else {
            xx = (int)(this.x*width);
            yy = (int)(this.y*height);
        }
        if(filled)
            r.fillRectangle(x+xx, y+yy, w, h, color);
        else
            r.drawRectangle(x+xx, y+yy, w, h, color);
    }
}
