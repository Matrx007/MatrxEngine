package com.engine.libs.game;

import com.engine.libs.input.Input;
import com.engine.libs.rendering.Renderer;

public abstract class Module {
    public boolean visible = true;
    public int depth = 0;
    public int x, y, width, height;
    public abstract void init();
    public abstract void update(Input i);
    public abstract void render(Renderer r, int x, int y, int width, int height);
}
