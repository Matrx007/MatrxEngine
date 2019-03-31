package com.engine.libs.nwm;

import com.engine.libs.game.GameObject;
import com.engine.libs.game.Mask;
import com.engine.libs.game.behaviors.AABBComponent;

import java.util.SplittableRandom;

/**
 * @author Rainis Randmaa
 * @version 1.0.0
 */
public abstract class WorldObject extends GameObject {
    protected final static SplittableRandom random = new SplittableRandom();
    public static int depthBuffer = 1024;
    public int width;
    public int height;
    public boolean collideable;
    private double prevX, prevY;
    public Mask.Rectangle mask;

    public WorldObject(double x, double y, int width, int height, int index, int depth, boolean collideable) {
        super(index, depth*depthBuffer+random.nextInt(depthBuffer));
        this.width = width;
        this.height = height;
        this.collideable = collideable;
        this.x = x;
        this.y = y;
        this.mask = new Mask.Rectangle(x, y, width, height);
        this.aabbComponent = new AABBComponent(this.mask);
        this.prevX = x;
        this.prevY = y;
    }

    /**
     * Needed for NWMWorld to work.
     */
    public abstract boolean equals(Object o);
    /**
     * Needed for NWMWorld to work.
     */
    public abstract int hashCode();
}
