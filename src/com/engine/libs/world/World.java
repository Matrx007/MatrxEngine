package com.engine.libs.world;

import com.engine.Core;
import com.engine.Game;
import com.engine.libs.exceptions.PrintError;
import com.engine.libs.game.GameObject;
import com.engine.libs.math.AdvancedMath;

import java.util.ArrayList;
import java.util.Comparator;

public class World {
    private ArrayList<GameObject> objects, addObjects, removeObjects;
    private Game g;

    public World(Game g) {
        objects = g.getE().objects;
        addObjects = g.getE().addQueue;
        removeObjects = g.getE().removeQueue;
        this.g = g;
    }

    public World() {
        objects = new ArrayList<>();
        addObjects = new ArrayList<>();
        removeObjects = new ArrayList<>();
        g = null;
    }

    public synchronized void remove(GameObject obj) {
        removeObjects.add(obj);
    }

    public synchronized void add(GameObject obj) {
        if(obj.index == -1) {
            PrintError.printError("World: Cannot add an object without an index"); } else {addObjects.add(obj);}
    }

    public void processQueue() {
        objects.addAll(addObjects);
        addObjects.clear();
        objects.removeAll(removeObjects);
        removeObjects.clear();
    }

    public GameObject closest(int x, int y, int index) {
        int closestDistance = -1;
        GameObject closestObject = null;
        boolean needFirst = true;
        for(GameObject obj: objects) {
            if(obj.index == index) {
                int distance = (int)AdvancedMath.distance(x, y, obj.xInt, obj.yInt);
                if(needFirst) {
                    closestDistance = distance;
                    closestObject = obj;
                    needFirst = false;
                } else if(distance < closestDistance) {
                    closestDistance = distance;
                    closestObject = obj;
                }
            }
        }
        return closestObject;
    }

    public GameObject furthest(int x, int y, int index) {
        int furthestDistance = -1;
        GameObject furthestObject = null;
        boolean needFirst = true;
        for(GameObject obj: objects) {
            if(obj.index == index) {
                int distance = (int)AdvancedMath.distance(x, y, obj.xInt, obj.yInt);
                if(needFirst) {
                    furthestDistance = distance;
                    furthestObject = obj;
                    needFirst = false;
                } else if(distance > furthestDistance) {
                    furthestDistance = distance;
                    furthestObject = obj;
                }
            }
        }
        return furthestObject;
    }

    public GameObject objectAt(int x, int y, int index) {
        for(GameObject obj: objects) {
            if(obj.index == index) {
                if(obj.mask.isColliding(x, y)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public ArrayList<GameObject> objectsAt(int x, int y, int index) {
        ArrayList<GameObject> objs = new ArrayList<>();
        for(GameObject obj : objects) {
            if(obj.index == index) {
                if(obj.mask.isColliding(x, y)) {
                    objs.add(obj);
                }
            }
        }
        return objs;
    }

    public ArrayList<GameObject> objectsAt(int x, int y) {
        ArrayList<GameObject> objs = new ArrayList<>();
        for(GameObject obj : objects) {
            if(obj.mask.isColliding(x, y)) {
                objs.add(obj);
            }
        }
        return objs;
    }

    public ArrayList<GameObject> solidsAt(int x, int y) {
        ArrayList<GameObject> objs = new ArrayList<>();
        for(GameObject obj : objects) {
            if(obj.solid) {
                if (obj.mask.isColliding(x, y)) {
                    objs.add(obj);
                }
            }
        }
        return objs;
    }

    public GameObject objectAtNotme(int x, int y, int index, GameObject me) {
        for(GameObject obj: objects) {
            if(obj.index == index && obj != me) {
                if(obj.mask.isColliding(x, y)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public void removeAll(int index) {
        for(GameObject obj: objects) {
            if(obj.index == index) {
                remove(obj);
            }
        }
    }

    public boolean solidAt(int x, int y, GameObject me) {
        for(GameObject obj: objects) {
            if(obj.solid && obj != me) {
                if(obj.mask.isColliding(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean solidAt(int x, int y) {
        for(GameObject obj: objects) {
            if(obj.solid) {
                if(obj.mask.isColliding(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clean() {
        for(GameObject obj: objects) {
            if(obj.dead) {
                objects.remove(obj);
            }
        }
    }

    public void render(Core e) {
        for(GameObject obj:objects) {
            obj.render(e.getRenderer());
        }
    }

    public GameObject find(int index, int n) {
        for (GameObject object : objects) {
            if (object.index == index) {
                if (n == 0) {
                    return object;
                } else {
                    n--;
                }
            }
        }

        return null;
    }

    public synchronized ArrayList<GameObject> getObjects() {
        return objects;
    }

    public Game getG() {
        return g;
    }

    public GameObject findObject(int index) {
        for(GameObject obj:objects) {
            if(obj.index == index) {
                return obj;
            }
        }
        return null;
    }

    public void update(Core e) {
        for(GameObject obj: objects) {
            if(obj.dead) {
                removeObjects.add(obj);
            } else {
                obj.$update$(e.getInput());
            }
        }

        processQueue();
    }

    public synchronized void fixDepth() {
        if(objects != null)
            objects.sort(Comparator.comparingInt(o -> o.depth));

        //getObjects().sort(Comparator.comparingInt(obj -> obj.depth));
    }

}
