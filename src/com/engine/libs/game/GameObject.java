package com.engine.libs.game;

import com.engine.Core;
import com.engine.libs.game.behaviors.AABBComponent;
import com.engine.libs.input.Input;
import com.engine.libs.rendering.Renderer;

public abstract class GameObject {
    public GameObject(int index, int depth) {
        this.index = index;
        this.depth = depth;
    }
    public GameObject() {
        this.depth = 0;
    }
    public int index = -1, depth, xInt, yInt;
    public double x, y;
    public AABBComponent aabbComponent;
    public boolean solid, dead;
    public Mask mask = null;
    protected Renderer r;
    protected Input i;
    protected Core core;

    public void $update$(Input i) {
        xInt = (int)x;
        yInt = (int)y;
        update(i);
    }

    public abstract void update(Input i);
    public abstract void render(Renderer r);
}
