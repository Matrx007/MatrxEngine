package com.engine.libs.game;

import com.engine.Core;

public abstract class renderObject {
    public int x, y, w, h, rotation, index;
    public boolean visible;

    public renderObject(int x, int y, int w, int h, int rotation, int index, boolean visible) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.rotation = rotation;
        this.index = index;
        this.visible = visible;
    }

    public abstract void render(Core Core);
}
