package com.engine.libs.game.behaviors;

import com.engine.libs.game.Mask;
import com.engine.libs.math.Point;
import com.engine.libs.rendering.Renderer;

import java.awt.*;

public class AABBComponent {
    public Mask area;
    public AABBComponent(Mask area) {
        this.area = area;
    }

    public boolean collides(AABBComponent component) {
        return (component.area.isColliding(area));
    }

    public boolean collides(Point point) {
        return (area.isColliding(point));
    }

    public void render(Renderer r) {
    }
}
