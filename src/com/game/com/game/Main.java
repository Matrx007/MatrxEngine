package com.game;

import com.engine.Core;
import com.engine.Game;
import com.engine.libs.graphics.renderEngine.Drawable;
import com.engine.libs.graphics.renderEngine.GraphicalSurface;
import com.engine.libs.graphics.renderEngine.Source;
import com.engine.libs.input.Input;
import com.engine.libs.rendering.Renderer;

import java.awt.*;


public class Main extends Game {
    private int w, h, mx, my;
    private String fps;
    private Input i;
    private Renderer r;
    private Color gray = Color.gray, black = Color.black, red = Color.red;

    public Main() {
        System.setProperty("sun.java2d.opengl","True");

        e.start();

        w = e.getWidth();
        h = e.getHeight();
        i = e.getInput();
        r = e.getRenderer();
        fps = String.valueOf(e.getFps());

        rectangle = new Source(new Drawable() {
            @Override
            public void render(Graphics2D g2) {
                g2.setColor(red);
                g2.fillRect(mx-32, my-32, 64, 64);
            }
        }, false);
        rectangleSurface = new GraphicalSurface(e.getSurfaceRenderer(), rectangle);
        rectangleSurface.setDimension( 64, 64);
        guiSurface = new GraphicalSurface(e.getSurfaceRenderer(), new Source(new Drawable(){
            @Override
            public void render(Graphics2D g2) {
                g2.setColor(gray);
                g2.fillRect(0, 0, w, h);
            }
        }, true), new Source(new Drawable(){
            @Override
            public void render(Graphics2D g2) {
                g2.setColor(Color.black);
                g2.setFont(new java.awt.Font("HP Simplified", java.awt.Font.PLAIN, 12));
                g2.drawString(fps, 8, 8);
            }
        }, true));
        guiSurface.setDimension(e.getWidth(), e.getHeight());

        e.run();
    }

    private Source rectangle;
    private GraphicalSurface rectangleSurface, guiSurface;

    @Override
    public void update(Core e) {
        // Game update code here
        fps = String.valueOf(e.getFps());
        mx = i.getMouseX();
        my = i.getMouseY();
    }

    @Override
    public void render(Core e) {
        r.fillRectangle(0, 0, w, h, gray);
        r.drawText(fps, 8, 8, 12, black);
        r.fillRectangle(mx-32, my-32, 64, 64, red);
    }
}
