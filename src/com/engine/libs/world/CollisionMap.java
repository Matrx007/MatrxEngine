package com.engine.libs.world;

import com.engine.libs.game.Mask;
import com.engine.libs.game.behaviors.AABBComponent;
import com.engine.libs.math.Point;

import java.util.ArrayList;

public class CollisionMap {
    private ArrayList<AABBComponent> components, addQueue, removeQueue;

    public CollisionMap() {
        components = new ArrayList<>();
        addQueue = new ArrayList<>();
        removeQueue= new ArrayList<>();
    }

    public void add(AABBComponent component) {
        addQueue.add(component);
    }

    public void remove(AABBComponent component) {
        removeQueue.add(component);
    }

    public void empty() {
        components.clear();
        addQueue.clear();
        removeQueue.clear();
    }

    public int size() {
        return components.size();
    }

    public void removeAt(int x, int y) {
        for(AABBComponent component: components) {
            if(component.collides(new Point(x, y))) {
                removeQueue.add(component);
            }
        }
    }

    public void refresh() {
        components.addAll(addQueue);
        addQueue.clear();
        components.removeAll(removeQueue);
        removeQueue.clear();
    }

    public boolean collisionAt(int x, int y) {
        Point point = new Point(x, y);
        for(AABBComponent component : components) {
            if(component.collides(point)) {
                return true;
            }
        }

        return false;
    }

    public boolean collisionAtExcept(int x, int y, AABBComponent except) {
        Point point = new Point(x, y);
        for(AABBComponent component : components) {
            if(component.collides(point) && component != except) {
                return true;
            }
        }

        return false;
    }

    public boolean collisionWith(Mask mask) {
        AABBComponent com = new AABBComponent(mask);
        for(AABBComponent component : components) {
            if(component.collides(com) && component != com) {
                return true;
            }
        }

        return false;
    }

    public boolean collisionWithExcept(Mask mask, AABBComponent except) {
        AABBComponent com = new AABBComponent(mask);
        for(AABBComponent component : components) {
            if(component.collides(com) && component != com && component != except) {
                return true;
            }
        }

        return false;
    }

    public AABBComponent componentAt(int x, int y) {
        Point point = new Point(x, y);
        for(AABBComponent component : components) {
            if(component.collides(point)) {
                return component;
            }
        }

        return null;
    }

    public AABBComponent componentAtExcept(int x, int y, AABBComponent except) {
        Point point = new Point(x, y);
        for(AABBComponent component : components) {
            if(component.collides(point) && component != except) {
                return component;
            }
        }

        return null;
    }
}
