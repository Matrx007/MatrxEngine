package com.engine.libs.graphics;

import com.engine.Core;

import java.util.ArrayList;

public abstract class Renderable {
    protected ArrayList<Shape> shapes;

    public void add(Shape shape) {
        shapes.add(shape);
    }

    public Shape get(int index) {
        return shapes.get(index);
    }

    public void render(Core e) {
        for(Shape shape : shapes) {
            e.getRenderer().render(shape);
        }
    }
}
