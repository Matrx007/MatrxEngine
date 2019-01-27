package com.engine;

import com.engine.libs.game.GameObject;
import com.engine.libs.game.Module;
import com.engine.libs.input.Input;
import com.engine.libs.rendering.Renderer;
import com.engine.libs.rendering.SurfaceRenderer;
import com.engine.libs.rendering.Window;

import javax.swing.*;
import java.awt.*;
import java.util.*;

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
    public static final String version = "0.2.802";
    private String title = "MatrxEngine - Gen II - Version "+version;

    private double frameTime;
    public boolean startAsFullscreen = false;
    private int frames;
    private int fps;
    private HashMap<String, Module> modules;
    private ArrayList<Module> modulesSortedByDepth;

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

    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        if (original_width > bound_width) {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        }

        if (new_height > bound_height) {
            new_height = bound_height;
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    private void startBootScreen() {
        com.engine.libs.rendering.Image image = new com.engine.libs.rendering.Image("/matrxenginebootscreen.png");

        Graphics2D g2 = (Graphics2D) Window.getCanvas().getGraphics();
        System.out.println(Window.getCanvas().getWidth());
        System.out.println(Window.getCanvas().getHeight());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setColor(Color.black);
        g2.fillRect(0, 0, Window.getCanvas().getWidth(), Window.getCanvas().getHeight());
        int wid = Window.getCanvas().getWidth();
        int hei = Window.getCanvas().getHeight();
        int w = Window.getCanvas().getWidth();
        int h = (int)(960d/1280d*(double) wid);
        g2.drawImage(image.getImage(),-((w-wid)/2), -((h-hei)/2), w, h, null);
        g2.setColor(Color.white);
        g2.drawString(version, 32, 32);

        Renderer = new Renderer(this);
        input = new Input(this);
        modules = new HashMap<>();
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
                game.update(Core.this);
                game.mouseX = input.mouseX;
                game.mouseY = input.mouseY;

                for (int i = modulesSortedByDepth.size()-1; i >= 0; i--) {
                    modulesSortedByDepth.get(i).update(input);
                }

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
            Module module;
            for (int i = modulesSortedByDepth.size()-1; i >= 0; i--) {
                module = modulesSortedByDepth.get(i);
                modulesSortedByDepth.get(i).render(getRenderer(), module.x, module.y, module.width, module.height);
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

    public void sortModules() {
        modulesSortedByDepth.clear();
        modulesSortedByDepth.addAll(modules.values());
        modulesSortedByDepth.sort(Comparator.comparingInt(m -> m.depth));
    }

    public void removeModule(String name) {
        modules.remove(name);
        sortModules();
    }

    public Module getModule(String name) {
        return modules.get(name);
    }

    public void addModule(String name, Module module) {
        modules.put(name, module);
        sortModules();
    }
}
