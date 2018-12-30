package com.engine;

import com.engine.libs.game.GameObject;
import com.engine.libs.input.Input;
import com.engine.libs.rendering.Renderer;
import com.engine.libs.rendering.SurfaceRenderer;
import com.engine.libs.rendering.Window;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Core implements Runnable {

    private com.engine.libs.rendering.Window Window;
    private com.engine.libs.rendering.Renderer Renderer;
    private Input input;

    private boolean running;
    private Thread rendering, updater;

    private static double UPDATE_CAP=1.0 / 60.0;

    public boolean disableJFrame = false;
    public JComponent target = null;

    public int width = 1280, height = 720;
    public float scale = 1f;
    // < 0.0.76 - MatrxEngine Classic,
    // < 0.1.32 - MatrxEngine Pro,
    // > 0.1.38 - MatrxEngine Ultimate,
    // > 0.2.000 - MatrxEngine Gen II
    public static final String version = "0.2.754";
    private String title = "MatrxEngine - Gen II - Version "+version;

    private double frameTime;
    public boolean startAsFullscreen = false;
    private int frames;
    private int fps;

    //private Thread processConnection;

    public ArrayList<GameObject> addQueue = new ArrayList<>();
    public ArrayList<GameObject> objects = new ArrayList<>();
    public ArrayList<GameObject> removeQueue = new ArrayList<>();

    public boolean BuildInRenderer = true;
    public boolean AutoUpdateWorld = true;

    private Game game;
    public boolean AutomaticDepthFix = false, UsesSurfaceRenderer = false;
    private SurfaceRenderer surfaceRenderer;

    public Core(Game game) {
        this.game = game;
    }
    public Core(Game game, double updateCap) {
        this.game = game;
        UPDATE_CAP = updateCap;
    }

    public synchronized void start() {
        Window = new Window(this, startAsFullscreen);
        surfaceRenderer = new SurfaceRenderer(this);
        startBootScreen();

        Thread thread = new Thread(this);
    }

    public SurfaceRenderer getSurfaceRenderer() {
        return surfaceRenderer;
    }

    private void startBootScreen() {
        com.engine.libs.rendering.Image image = new com.engine.libs.rendering.Image("/matrxenginebootscreen.png");

        Graphics2D g2 = (Graphics2D) Window.getCanvas().getGraphics();
        g2.setColor(Color.black);
        g2.fillRect(0, 0, Window.getCanvas().getWidth(), Window.getCanvas().getHeight());
        g2.drawImage(image.getImage(),(getCanvas().getWidth()-image.getW())/2, (getCanvas().getHeight()-image.getH())/2, null);
        g2.drawString(version, 8, 8);

        Renderer = new Renderer(this);
        input = new Input(this);

    }

    private void stopBootScreen() {
        Window.getCanvas().getGraphics().setColor(Color.black);
        Window.getCanvas().getGraphics().fillRect(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
    }

    public void run() {
        stopBootScreen();

        double frameTime = 0;
        int frames = 0;
        fps = 0;

        running = true;

        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;
        while (running) {
            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while (unprocessedTime >= UPDATE_CAP) {
                unprocessedTime -= UPDATE_CAP;

                //TODO: Update game

                /*if(!processConnection.isAlive()) {
                    processConnection.start();
                }*/

                game.update(Core.this);
                game.mouseX = input.mouseX;
                game.mouseY = input.mouseY;

                //Iterator<GameObject> objs = game.getObjects().iterator();

                if(AutoUpdateWorld) {
                    for (Iterator<GameObject> it = objects.iterator(); it.hasNext(); ) {
                        GameObject obj = it.next();
                        if (!obj.dead) {
                            obj.$update$(input);
                        } else {
                            it.remove();
                        }
                    }
                }

                try {
                    if(game.getWorld() != null) {
                        objects.addAll(addQueue);
                        addQueue.clear();
                        objects.removeAll(removeQueue);
                        removeQueue.clear();
                        if(AutomaticDepthFix) {
                            game.getWorld().fixDepth();
                        }
                    }
                } catch (NullPointerException e) {
                    // Wait until everything is initialized
                }

                input.update();

                if (frameTime >= 1) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
            }

            //TODO: Render game

            Window.update();
            if(UsesSurfaceRenderer) {
                surfaceRenderer.render();
            } else {
                game.render(this);
            }
            if (BuildInRenderer) {

                for (Iterator<GameObject> it = objects.iterator(); it.hasNext(); ) {
                    GameObject obj = it.next();
                    if (!obj.dead) {
                        obj.render(getRenderer());
                    }
                }
            }

            frames++;
        }
    }

    public void stop() {
        running = false;
        rendering.interrupt();
        updater.interrupt();
    }

    public  void dispose() {

    }

    public void kill() {
        rendering.interrupt();
        rendering = null;
        updater.interrupt();
        updater = null;
        running = false;
        Window.getFrame().setVisible(false);
    }

    public void setUpdateCap(int ticksPerSecond) {
        UPDATE_CAP = 1.0 / (double)ticksPerSecond;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getScale() {
        return scale;
    }

    public String getTitle() {
        return title;
    }

    public Window getWindow() { return Window; }

    public Canvas getCanvas() { return Window.getCanvas(); }

    public Input getInput() { return input; }

    public com.engine.libs.rendering.Renderer getRenderer() {return Renderer; }

    public int getFps() {
        return fps;
    }
}
