package com.engine;

import com.engine.libs.game.GameObject;
import com.engine.libs.game.renderObject;
import com.engine.libs.world.World;

import java.util.ArrayList;

public abstract class Game {

    protected Core e;
    public World world;
    private ArrayList<renderObject> tiles;
    protected int mouseX, mouseY;

    public Game() {
        e = new Core(this);
        world = new World(this);
        tiles = new ArrayList<>();
    }

    public abstract void update(Core e);
    public abstract void render(Core e);

    protected boolean isButton(boolean isRight) {
        return e.getInput().isButton((isRight) ? 3 : 1);
    }

    protected boolean isButtonPressed(boolean isRight) {
        return e.getInput().isButtonDown((isRight) ? 3 : 1);
    }

    protected boolean isButtonReleased(boolean isRight) {
        return e.getInput().isButtonUp((isRight) ? 3 : 1);
    }

    public ArrayList<GameObject> getObjects() {
        return e.objects;
    }

    public GameObject getObject(int id) {
        return e.objects.get(id);
    }

    public void addObject(GameObject obj) {e.addQueue.add(obj); }

    public Core getE() {
        return e;
    }

    public World getWorld() {
        return world;
    }
}
