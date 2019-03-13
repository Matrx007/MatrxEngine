package com.engine.libs.world;

import com.engine.libs.game.GameObject;
import com.engine.libs.math.AdvancedMath;
import com.engine.libs.math.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class ChunkWorld<E> {
    public final HashMap<Point, ArrayList<E>> chunks;
    private HashMap<Point, ArrayList<E>> addChunks;
    private HashMap<Point, ArrayList<E>> removeChunks;
    private int chunkSize;

    public ChunkWorld(int chunkSize) {
        this.chunkSize = chunkSize;
        chunks = new HashMap<>();
        addChunks = new HashMap<>();
        removeChunks = new HashMap<>();
    }

    private ArrayList<E> fetchAddChunk(int x, int y) {
        Point cords = new Point(x, y);
        if(addChunks.containsKey(cords)) {
            return addChunks.get(cords);
        } else {
            ArrayList<E> list = new ArrayList<>();
            addChunks.put(cords, list);
            return list;
        }
    }
    private ArrayList<E> fetchRemoveChunk(int x, int y) {
        Point cords = new Point(x, y);
        if(removeChunks.containsKey(cords)) {
            return removeChunks.get(cords);
        } else {
            ArrayList<E> list = new ArrayList<>();
            removeChunks.put(cords, list);
            return list;
        }
    }
    public ArrayList<E> fetchChunk(int x, int y) {
        Point cords = new Point(x, y);
        if(chunks.containsKey(cords)) {
            return chunks.get(cords);
        } else {
            ArrayList<E> list = new ArrayList<>();
            chunks.put(cords, list);
            return list;
        }
    }

    public void addObject(int x, int y, E obj) {
        fetchAddChunk(x, y).add(obj);
    }
    public void removeObject(int x, int y, E obj) {
        fetchAddChunk(x, y).remove(obj);
    }

    public int chunk(long pixel) {
        return (int)(pixel / chunkSize);
    }
    public int chunk(int pixel) {
        return (pixel / chunkSize);
    }

    // x, y, w, h - Mask, measured in chunks
    public ArrayList<E> getRange(int x, int y, int w, int h) {
        ArrayList<E> ret = new ArrayList<>();

        for(int i = x+w-1; i >= x; i--) {
            for(int j = y+h-1; j >= y; j--) {
                ret.addAll(fetchChunk(i, j));
            }
        }

        return ret;
    }

    // x, y, w, h - Mask, measured in pixels
    // bufferZone - Additional are around the mask, measured in chunks
    public ArrayList<E> getColliding(int x, int y, int w, int h, int bufferZone) {
        ArrayList<E> ret = new ArrayList<>();

        int chunkX =  (int)Math.floor(x/chunkSize)-bufferZone;
        int chunkY =  (int)Math.floor(y/chunkSize)-bufferZone;
        int chunkBX = (int)Math.ceil((x+w)/chunkSize)-bufferZone;
        int chunkBY = (int)Math.ceil((y+h)/chunkSize)-bufferZone;

        for(int i = chunkBX-1; i >= chunkX; i--) {
            for(int j = chunkBY-1; j >= chunkY; j--) {
                ArrayList<E> chunk = fetchChunk(i, j);

                for(E obj : chunk) {
                    if(collides(x, y, w, h, obj)) {
                        ret.add(obj);
                    }
                }
            }
        }

        return ret;
    }

    public boolean collides(int x, int y, int w, int h, E obj) {
        if(obj instanceof GameObject) {
            return AdvancedMath.inRange(x, y, w, h,
                    ((GameObject) obj).x, ((GameObject) obj).y);
        } else return false;
    }

    public void forEach(Consumer<E> forEach) {
        for(ArrayList<E> chunk : chunks.values()) {
            chunk.forEach(forEach);
        }
    }

    public void dump() {
        for(Point p : addChunks.keySet()) {
            ArrayList<E> chunk;
            if(!chunks.containsKey(p)) {
                chunk = new ArrayList<>();
                chunks.put(p, chunk);
            } else chunk = chunks.get(p);
            chunk.addAll(addChunks.get(p));
        }
        for(Point p : removeChunks.keySet()) {
            ArrayList<E> chunk;
            if(!chunks.containsKey(p)) {
                chunk = new ArrayList<>();
                chunks.put(p, chunk);
            } else chunk = chunks.get(p);
            chunk.removeAll(removeChunks.get(p));
        }
        removeChunks.clear();
        addChunks.clear();
    }
}
