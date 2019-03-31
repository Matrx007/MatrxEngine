package com.engine.libs.nwm;

import com.engine.libs.game.Mask;

import java.util.*;

/**
 * Masked freeform global world
 *
 * Masked - Objects are targeted by their masks,
 *          used for rendering only objects that
 *          are visible
 * Freeform - Objects are not in grid or in any
 *            kind of pattern.
 * Global - All objects of the world are being
 *          updated
 *
 * @author Rainis Randmaa
 * @version 1.0.0
 */
public class NWMWorld {
    private HashSet<WorldObject> objects;

    public NWMWorld() {
        objects = new HashSet<>();
    }

    /**
     * Add objects into the world
     *
     * @param object Object to add into the world
     */
    public void addObject(WorldObject object) {
        objects.add(object);
    }

    /**
     * Remove objects from the world
     *
     * @param object Target object
     */
    public void removeObject(WorldObject object) {
        objects.remove(object);
    }

    /**
     * Get objects within the area, useful for rendering
     * only the objects that are visible.
     *
     * @param areaX Area's top left corner X coordinate
     * @param areaY Area's top left corner Y coordinate
     * @param areaW Area's bottom right corner X coordinate
     * @param areaH Area's bottom right corner Y coordinate
     * @return Set of objects touching the area
     */
    public HashSet<WorldObject> getObjects(int areaX, int areaY,
                                           int areaW, int areaH) {
        Mask.Rectangle mask = new Mask.Rectangle(
                areaX, areaY, areaW, areaH);
        HashSet<WorldObject> result = new HashSet<>();
        objects.forEach(o -> {
            if(o.mask.isColliding(mask)) {
                result.add(o);
            }
        });
        return result;
    }

    /**
     * Sorts objects by their depth value. Mainly
     * used with getObjects().
     *
     * @param unsorted Set of unsorted objects
     * @return Set of objects sorted by their depth values
     */
    public LinkedHashSet<WorldObject> sortByDepth(
            HashSet<WorldObject> unsorted) {
        List<WorldObject> list = new ArrayList<>(unsorted);
        list.sort((o1, o2) -> o2.depth - o1.depth);
        return new LinkedHashSet<>(list);
    }

    /**
     * The native access to the Set of objects
     * @return The Set of objects in the world
     */
    public HashSet<WorldObject> getObjects() {
        return objects;
    }

    /**
     * Used for targeting specific chunks
     */
    private static class Location {
        private final int x;
        private final int y;

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
