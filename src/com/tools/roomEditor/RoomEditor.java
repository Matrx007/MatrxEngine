package com.tools.roomEditor;

import com.engine.Core;
import com.engine.Game;
import com.engine.libs.font.Alignment;
import com.engine.libs.game.GameObject;
import com.engine.libs.math.AdvancedMath;
import com.engine.libs.rendering.Renderer;
import org.json.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoomEditor extends Game {
    private static String path = null;
    private ArrayList<ObjectSpecs> objectSpecs;
    int gridW, gridH, gridOffsetX, gridOffsetY;
    boolean gridVisible, waitingForGridDialog;
    private SizeDialog gridSizeDialog;
    private int scroll;
    private boolean draggingNewObject, waitingForObjectCreator;
    private ObjectSpecs draggingObject;
    private HashMap<String, BufferedImage> preloadedImages;
    private BufferedImage draggingImage;
    private ObjectCreator objectCreator;
    private CopyOnWriteArrayList<Object> placedObjects;
    private Renderer r;
    private JSONObject exportedJSON;
    private SimpleDateFormat reportDateFormat;

    private int objectState;
    private static final int OBJ_STATE_NORMAL = 0, OBJ_STATE_DRAGGING = 1, OBJ_STATE_ROTATE = 2, OBJ_STATE_RESIZE = 3, OBJ_STATE_MOVE_ANCHOR = 4;

    private int state;
    private static final int STATE_IDLE = 0,
                             STATE_PLACING = 1,
                             STATE_ROTATING = 2,
                             STATE_DRAGGING = 3,
                             STATE_RESIZING = 4;

    public RoomEditor(String projectFolder, boolean load) {
        e.start();

        // Init
        initializeVariables();

        // Creates missing directories for resources
        if(new File(projectFolder).isDirectory()) {
            path = projectFolder;
            File temp = new File(path + File.separator + "res");
            temp.mkdir();
        }

        if(load) {
            if (path != null) {
                File file = new File(path + File.separator + "roomEditor_data.red");
                System.out.println(file.getAbsolutePath());
                if (file.exists()) {
                    try {
                        StringBuilder rawJson = new StringBuilder();
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line = reader.readLine().replace("\\","");
                        System.out.println(line);
                        exportedJSON = new JSONObject(line);

                        loadJSON();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                return;
            }
        } // If in load mode, load the game from the root file
        else {
            File file = new File(path + File.separator + "roomEditor_data.red");
            if(file.exists()) {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } // If in save mode, delete the root file and create new one

        // Adds WindowAdapter to confirm to save before exiting.
        e.getWindow().getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Create confirm dialog and get the state.
                // If yes - Save the game and close.
                // If no - Close the game without saving.
                // If cancel - Do nothing.
                int state = JOptionPane.showConfirmDialog(getE().getWindow().getFrame(), "Save changes?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
                if(state == JOptionPane.CANCEL_OPTION) {
                    super.windowClosing(e);
                }
                else if(state == JOptionPane.YES_OPTION) {
                    exportJSON();

                    if (path != null) {
                        File file = new File(path + File.separator + "roomEditor_data.red");
                        try {
                            if (!file.exists()) {
                                //noinspection ResultOfMethodCallIgnored
                                file.createNewFile();
                                System.out.println("Created new JSON file!");
                            }
                            FileWriter writer = new FileWriter(file);
                            String text = exportedJSON.toString().replace("\\", "");
                            report("Saved JSON: "+text);
                            writer.write(text);
                            writer.flush();
                            writer.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                else if(state == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        }); // Saving confirm / SAVE

        e.run();
    }

    private void initializeVariables() {
        r = e.getRenderer();
        g2 = (Graphics2D) r.getG();
        scroll = 0;
        objectSpecs = new ArrayList<>();
        draggingNewObject = false;
        draggingObject = null;
        preloadedImages = new HashMap<>();
        draggingImage = null;
        waitingForObjectCreator = false;
        objectCreator = null;
        placedObjects = new CopyOnWriteArrayList<>();
        state = STATE_IDLE;
        waitingForGridDialog = false;
        gridOffsetX = 0;
        gridOffsetY = 0;
        gridW = 32;
        gridH = 32;
        gridVisible = true;
        gridSizeDialog = null;
        reportDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        objectState = OBJ_STATE_NORMAL;
    }

    private void exportJSON() {
        try {
            JSONObject jObject = new JSONObject();

            JSONArray jObjects = new JSONArray();
            placedObjects.forEach(o -> {
                try {
                    jObjects.put(new JSONObject(o.shareSend()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
            jObject.put("objects", jObjects);

            JSONArray types = new JSONArray();
            for(ObjectSpecs specs : objectSpecs) {
                JSONObject type = new JSONObject();
                type.put("w", specs.w);
                type.put("h", specs.h);
                type.put("alignX", specs.alignX);
                type.put("alignY", specs.alignY);
                type.put("angle", specs.angle);
                type.put("depth", specs.depth);
                type.put("name", specs.name);
                type.put("fileName", specs.imageFile);
                types.put(type);
            }
            jObject.put("types", types);

            exportedJSON = jObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadJSON() {
        try {
            JSONArray types = exportedJSON.getJSONArray("types");

            objectSpecs = new ArrayList<>();
            for(int i = 0; i < types.length(); i++) {
                JSONObject thatOne = types.getJSONObject(i);

                BufferedImage img = getImage(thatOne.getString("fileName"));

                objectSpecs.add(new ObjectSpecs(
                        thatOne.getString("name"),
                        thatOne.getString("fileName"),
                        thatOne.getInt("w"),
                        thatOne.getInt("h"),
                        thatOne.getInt("alignX"),
                        thatOne.getInt("alignY"),
                        thatOne.getInt("depth"),
                        thatOne.getInt("angle"),
                        null,
                        img
                ));
            }

            JSONArray objects = exportedJSON.getJSONArray("objects");

            this.placedObjects = new CopyOnWriteArrayList<>();
            for(int i = 0 ; i < objects.length(); i++) {
                this.placedObjects.add(new Object(objects.getJSONObject(i).toString()));
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private boolean mouseInsideSidePanel;

    @Override
    public void update(Core e) {
        if (waitingForObjectCreator) {
            System.out.println("return");
            return;
        }

        mouseInsideSidePanel = mouseX < 192;

        if (mouseInsideSidePanel) {
            UITick();
        }

        if(e.getInput().isKey(KeyEvent.VK_CONTROL)) {
            if(e.getInput().isKey(KeyEvent.VK_D)) objectState = OBJ_STATE_DRAGGING;
            if(e.getInput().isKey(KeyEvent.VK_R)) objectState = OBJ_STATE_ROTATE;
            if(e.getInput().isKey(KeyEvent.VK_S)) objectState = OBJ_STATE_RESIZE;
            if(e.getInput().isKey(KeyEvent.VK_N)) objectState = OBJ_STATE_NORMAL;
            if(e.getInput().isKey(KeyEvent.VK_A)) objectState = OBJ_STATE_MOVE_ANCHOR;
            if(e.getInput().isKey(KeyEvent.VK_G)) {
                gridVisible = !gridVisible;
                if(gridVisible) {
                    e.getInput().keys = new boolean[e.getInput().NUM_KEYS];
                    gridSizeDialog = new SizeDialog(this);
                } else {
                    gridSizeDialog = null;
                }
            }
        }

        if (!mouseInsideSidePanel && e.getInput().isButtonDown(1)) {
            if (state == STATE_IDLE)
                state = STATE_DRAGGING;
        }

        if(state != STATE_IDLE) {
            placedObjects.forEach(o -> o.$update$(e));
        }

        if (mouseInsideSidePanel && !draggingNewObject) handleSideBar();
        else handleDraggingNewObject();

        if(waitingForObjectCreator)
            waitForObjectCreator();

        placedObjects.sort(Comparator.comparingInt(o -> o.depth));
    }

    private void handleDraggingNewObject() {
        if (draggingNewObject) {
            if (e.getInput().isButtonUp(1)) {
                draggingNewObject = false;
                placedObjects.add(new Object(mouseX, mouseY, draggingObject.w, draggingObject.h,
                        draggingObject.alignX, draggingObject.alignY, draggingObject.depth,
                        draggingObject.angle, draggingObject.imageFile, draggingImage,
                        draggingObject.parameters));
                draggingObject = null;
                draggingImage = null;
            }
        }
    }

    private void waitForObjectCreator() {
        if (objectCreator.done) {
            waitingForObjectCreator = false;
            objectCreator.done = false;
            if (objectCreator.name == null) return;
            if (objectCreator.fileName == null) return;

            BufferedImage img = getImage(objectCreator.fileName);

            objectSpecs.add(new ObjectSpecs(objectCreator.name,
                    objectCreator.fileName, objectCreator.width,
                    objectCreator.height, objectCreator.alignX,
                    objectCreator.alignY, objectCreator.depth,
                    objectCreator.angle, null, img));
        }
    }

    private void handleSideBar() {
        handleSideBarObjectPicking();
        handleAddButton();
        handleSideBarScrolling();
    }

    private void handleSideBarObjectPicking() {
        int y = 32-scroll;
        for (ObjectSpecs obj : objectSpecs) {
            if (AdvancedMath.inRange(mouseX, mouseY, 0, y, 192, 64)) {
                if (e.getInput().isButtonDown(1) && !draggingNewObject) {
                    draggingNewObject = true;
                    draggingObject = obj;
                    BufferedImage img;
                    if(preloadedImages.containsKey(draggingObject.imageFile)) {
                        img = preloadedImages.get(draggingObject.imageFile);
                    } else {
                        img = getImage(draggingObject.imageFile);
                        if(img == null) {
                            draggingNewObject = false;
                            draggingImage = null;
                            draggingObject = null;
                        } else
                        preloadedImages.put(draggingObject.imageFile, img);
                    }
                    draggingImage = img;
                }
            }
            y += 64;
        }
    }

    private void handleAddButton() {
        if (AdvancedMath.inRange(mouseX, mouseY, 0, 32-scroll+objectSpecs.size()*64, 192, 64)) {
            if (e.getInput().isButtonDown(1)) {
                objectCreator = new ObjectCreator();
                waitingForObjectCreator = true;
            }
        }
    }

    private void handleSideBarScrolling() {
        if(e.getInput().getScroll() != 0) {
            if (e.getInput().getScroll() > 0) {
                scroll += 64;
            } else if (e.getInput().getScroll() < 0) {
                scroll -= 64;
            }
            e.getInput().scroll = 0;
            scroll = Math.max(0, Math.min(scroll, Math.max(0, objectSpecs.size()*64+64+32-e.height+128)));
        }
    }

    private BufferedImage getImage(String name) {
        report("Getting image: "+name);
        if(preloadedImages.containsKey(name)) {
            report("Image already loaded: "+name);
            return preloadedImages.get(name);
        } else {
            BufferedImage img = readImage(name);
            preloadedImages.put(name, img);
            report("Added new image: "+name);
            return img;
        }
    }

    private BufferedImage readImage(String name) {
        try {
            return ImageIO.read(new File(path + File.separator + "res" + File.separator + name));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void report(String s) {
        System.out.println("["+reportDateFormat.format(new Date())+"]"+" "+s);
    }

    private Graphics2D g2;

    @Override
    public void render(Core e) {
        if(waitingForObjectCreator) {
            return;
        }

        r.absolute();

        r.fillRectangle(192, 0, e.width-192, e.height, Color.white);

        if(gridVisible) {
            int offX = (gridOffsetX + r.getCamX()) % gridW;
            int offY = (gridOffsetY + r.getCamY()) % gridH;

            for (int yy = 0; yy < Math.ceil(e.height / gridH)+1; yy++) {
                r.drawLine(offX, offY + yy * gridH, offX + e.width, offY + yy * gridH, Color.black);
            }

            for (int xx = (int)Math.floor(192/gridW); xx < Math.ceil(e.width / gridW)+1; xx++) {
                r.drawLine(offX + xx * gridW, offY, offX + xx * gridW, offY + e.height, Color.black);
            }
        }
        r.fillRectangle(0, 0, 192, e.height, new Color(32, 32, 32));
        r.relative();
        for(GameObject obj : placedObjects) {
            obj.render(e);
        }

        r.absolute();
        UIRender();

        // Draw object overlay when placing
        if(draggingNewObject) {
            AffineTransform previous = g2.getTransform();
            g2.setTransform(AffineTransform.getRotateInstance(Math.toRadians(draggingObject.angle), mouseX, mouseY));
            g2.drawImage(draggingImage, mouseX-draggingObject.alignX, mouseY-draggingObject.alignY,
                    draggingObject.w, draggingObject.h, null);
            g2.setTransform(previous);
        }
    }

    // UI methods and variables
    private boolean addButtonHover;
    private int buttonHoverID;

    private Alignment alignmentHorRightVerMiddle =
            new Alignment(Alignment.HOR_RIGHT, Alignment.VER_MIDDLE);
    private Alignment alignmentHorCenterVerMiddle =
            new Alignment(Alignment.HOR_CENTER, Alignment.VER_MIDDLE);

    private void UITick() {
        addButtonHover = AdvancedMath.inRange(mouseX, mouseY, 0, 32+objectSpecs.size()*64, 192, 64);

        for(int i = 0 ; i < objectSpecs.size(); i++) {
            if(AdvancedMath.inRange(mouseX, mouseY, 0, 32+i*64, 192, 64)) {
                buttonHoverID = i;
                break;
            }
        }
    }

    private void UIRender() {
        int y = 32-scroll;
        boolean everySecond = false;
        for(int i = 0; i < objectSpecs.size(); i++) {
            ObjectSpecs obj = objectSpecs.get(i);
            r.fillRectangle(0, y, 192, 64, new Color(0, 0, 0, (everySecond) ? 32+8 : 32));
            if(buttonHoverID == i)
                r.fillRectangle(0, y, 192, 64, new Color(0, 0, 0, 32));
            r.drawImage(16, y+16, 32, 32, obj.image);
            r.drawText(obj.name, 64, y+16, 24, alignmentHorRightVerMiddle, new Color(255, 255, 255, 128));
            y+=64;
            everySecond = !everySecond;
        }

        // Add button
        if(addButtonHover)
            r.fillRectangle(0, y, 192, 64, new Color(128, 255, 128, 96));
        else
            r.fillRectangle(0, y, 192, 64, new Color(0, 255, 0, 96));
        r.drawText("+", 96, y+32, 64, alignmentHorCenterVerMiddle, Color.black);

//        r.fillRectangle(192, 0, e.width-192, 32, new Color(32, 32, 32));
    }

    // Buffer class, holds data
    private class ObjectSpecs {
        private String name, imageFile;
        private int w, h, alignX, alignY, depth, angle;
        private ArrayList<String> parameters;
        private BufferedImage image;

        private ObjectSpecs(String name, String imageFile, int w, int h, int alignX, int alignY, int depth,
                            int angle, ArrayList<String> parameters, BufferedImage image) {
            this.name = name;
            this.imageFile = imageFile;
            this.w = w;
            this.h = h;
            this.alignX = alignX;
            this.alignY = alignY;
            this.depth = depth;
            this.angle = angle;
            this.parameters = parameters;
            this.image = image;
        }
    }

    private class Object extends GameObject {
        private int w, h, alignX, alignY, angle, dragX, dragY, radius, prevAngle, xx, yy;
        private int xLeftSide, yLeftSide, xTopSide, yTopSide, xRightSide, yRightSide, xBottomSide, yBottomSide, resizingStartSize, resizingStartDistance, anchorX, anchorY;
        private int resizingMiddleX, resizingMiddleY, oldX, oldY;
        private BufferedImage image;
        private ArrayList<String> parameters;
        private String imageFile;
        private boolean selected, dragging, rotating, resizing, resizingVer, movingAnchor;

        // Create Object from String with the help of
        // shareReceive method.
        // Used when loading game
        public Object(String data) {
            shareReceive(data);
            objectsLateInit();
        }

        // Creates object with all needed arguments.
        // Used when placing new object.
        public Object(int x, int y, int w, int h, int alignX, int alignY, int depth,
                      int angle, String imageFile, BufferedImage image, String... parameters) {
            new Object(x, y, w, h, alignX, alignY, depth, angle, imageFile, image, new ArrayList<>(Arrays.asList(parameters)));
        }
        public Object(int x, int y, int w, int h, int alignX, int alignY, int depth,
                      int angle, String imageFile, BufferedImage image, ArrayList<String> parameters) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.alignX = alignX;
            this.alignY = alignY;
            this.depth = depth;
            this.parameters = parameters;
            this.image = image;
            this.angle = angle;
            this.imageFile = imageFile;
            objectsLateInit();
        }

        private void objectsLateInit() {
            selected = false;
            dragging = false;
            rotating = false;
            dragX = 0;
            dragY = 0;
            radius = (int)Math.sqrt(w*w+h*h);
            cos = (int) (Math.cos(Math.toRadians(angle)) * radius / 2);
            sin = (int) (Math.sin(Math.toRadians(angle)) * radius / 2);
            resizing = false;
            calcObjectAreaPolygon();
        }

        // Used for rotation button location
        private int cos;
        private int sin;
        private int tempXX;
        private int tempYY;

        // Objects clickable area
        private Polygon area;

        private void calcMiddleXY() {
            xx = x + lengthDir_x(h/2-alignY, angle+90) - lengthDir_y(w/2-alignX, angle-90);
            yy = y + lengthDir_y(h/2-alignY, angle+90) + lengthDir_x(w/2-alignX, angle-90);
        }

        private void calcObjectAreaPolygon() {
            int cos1, sin1, cos2, sin2, cos3 ,sin3, cos4, sin4, hW = w/2, hH = h/2;
//            cos1 = lengthDir_x(-alignY, angle+90) - lengthDir_y(-alignX, angle-90);
//            sin1 = lengthDir_y(-alignY, angle+90) + lengthDir_x(-alignX, angle-90);
//
//            cos2 = lengthDir_x(-alignY, angle+90) - lengthDir_y(w-alignX, angle-90);
//            sin2 = lengthDir_y(-alignY, angle+90) + lengthDir_x(w-alignX, angle-90);
//
//            cos3 = lengthDir_x(h-alignY, angle+90) - lengthDir_y(w-alignX, angle-90);
//            sin3 = lengthDir_y(h-alignY, angle+90) + lengthDir_x(w-alignX, angle-90);
//
//            cos4 = lengthDir_x(h-alignY, angle+90) - lengthDir_y(-alignX, angle-90);
//            sin4 = lengthDir_y(h-alignY, angle+90) + lengthDir_x(-alignX, angle-90);

            cos1 = lengthDir_x(-alignY, angle+90) - lengthDir_y(-alignX, angle-90);
            sin1 = lengthDir_y(-alignY, angle+90) + lengthDir_x(-alignX, angle-90);

            cos2 = lengthDir_x(-alignY, angle+90) - lengthDir_y(w-alignX, angle-90);
            sin2 = lengthDir_y(-alignY, angle+90) + lengthDir_x(w-alignX, angle-90);

            cos3 = lengthDir_x(h-alignY, angle+90) - lengthDir_y(w-alignX, angle-90);
            sin3 = lengthDir_y(h-alignY, angle+90) + lengthDir_x(w-alignX, angle-90);

            cos4 = lengthDir_x(h-alignY, angle+90) - lengthDir_y(-alignX, angle-90);
            sin4 = lengthDir_y(h-alignY, angle+90) + lengthDir_x(-alignX, angle-90);

            int x1 = x+cos1;
            int y1 = y+sin1;
            int x2 = x+cos2;
            int y2 = y+sin2;
            int x3 = x+cos3;
            int y3 = y+sin3;
            int x4 = x+cos4;
            int y4 = y+sin4;

            area = new Polygon(new int[]{x1, x2, x3 ,x4},
                    new int[]{y1, y2, y3, y4},
                    4);
        }

        private void calcCosAndSin() {
            if(prevAngle != angle) {
                cos = (int) (Math.cos(Math.toRadians(angle)) * radius / 2);
                sin = (int) (Math.sin(Math.toRadians(angle)) * radius / 2);
            }
        }

        @Override
        public void update(Core e) {
            int prevX = x;
            int prevY = y;
            prevAngle = angle;
            calcCosAndSin();
            if(selected && objectState == OBJ_STATE_RESIZE)
                handleResizingObject();
            if(!resizing)
                handleSelectingObjectWithMouse();
            handleObjectsBehaviour();

            if(selected && e.getInput().isKeyDown(KeyEvent.VK_DELETE)) {
                placedObjects.remove(this);
            }

            if((angle != prevAngle) ||
                    (x != prevX) || (y != prevY)) {
                calcMiddleXY();
            }
        }

        private void handleObjectsBehaviour() {
            if(selected) {

                switch (objectState) {
                    case OBJ_STATE_RESIZE:
                        calcObjectResizePoints();
                        break;
                    case OBJ_STATE_ROTATE:
                        handleRotatingWithMouse();
                        break;
                    case OBJ_STATE_DRAGGING:
                        handleDraggingObjectWithMouse();
                        break;
                    case OBJ_STATE_MOVE_ANCHOR:
                        handleMoveAnchorWithMouse();
                        break;
                }
            } else {
                rotating = false;
            }
        }

        private void handleRotatingWithMouse() {
            if(e.getInput().isButtonDown(1)) {

                if(AdvancedMath.inRadius(mouseX, mouseY, x+cos, y+sin, 12)) {
                    rotating = true;
                }
            }

            if(rotating){
                int newAngle = (int)AdvancedMath.angle(x, y, mouseX, mouseY) + 90;

                if(angle != newAngle) {
                    angle = newAngle;
                }
            }

            if(e.getInput().isButtonUp(1)) {
                rotating = false;
                calcObjectAreaPolygon();
                cos = (int) (Math.cos(Math.toRadians(angle)) * radius / 2);
                sin = (int) (Math.sin(Math.toRadians(angle)) * radius / 2);
            }
        }

        private void calcObjectResizePoints() {
            calcMiddleXY();

            int cos1, sin1, cos2, sin2, cos3, sin3, cos4, sin4;

            int hW = w/2;
            int hH = h/2;

            cos1 = lengthDir_x(-hH-32, angle+90);
            sin1 = lengthDir_y(-hH-32, angle+90);
            cos2 =                               - lengthDir_y(-hW-32, angle-90);
            sin2 =                               + lengthDir_x(-hW-32, angle-90);
            cos3 = lengthDir_x(hH+32, angle+90);
            sin3 = lengthDir_y(hH+32, angle+90);
            cos4 =                               - lengthDir_y(hW+32, angle-90);
            sin4 =                               + lengthDir_x(hW+32, angle-90);

            xLeftSide = xx+cos1;
            yLeftSide = yy+sin1;
            xTopSide = xx+cos2;
            yTopSide = yy+sin2;
            xRightSide = xx+cos3;
            yRightSide = yy+sin3;
            xBottomSide = xx+cos4;
            yBottomSide = yy+sin4;
        }

        private void handleDraggingObjectWithMouse() {
            if(mouseX > 192) {

                // Check if not draggingNewObject rotating button
                if(!AdvancedMath.inRadius(mouseX, mouseY, x+cos, y+sin, 12)) {
                    if(area.contains(mouseX, mouseY)) {
                        if(e.getInput().isButtonDown(1)) {
                            dragging = true;
                            selected = true;
                            dragX = mouseX - x;
                            dragY = mouseY - y;
                        }
                    } else {
                        if(e.getInput().isButtonDown(1)) {
                            selected = false;
                        }
                    }

                    if(e.getInput().isButtonUp(1)) {
                        dragging = false;
                        calcObjectAreaPolygon();
                    }

                    if(dragging) {
                        x = mouseX - dragX;
                        y = mouseY - dragY;
                    }
                } else {
                    dragging = false;
                }

            } else {
                dragging = false;
            }
        }

        private void handleSelectingObjectWithMouse() {
            if(mouseX > 192) {

                // Check if not draggingNewObject rotating button
                if(!AdvancedMath.inRadius(mouseX, mouseY, x+cos, y+sin, 12)) {
                    if(area.contains(mouseX, mouseY)) {
                        if(e.getInput().isButtonDown(1)) {
                            selected = true;
                        }
                    } else {
                        if(e.getInput().isButtonDown(1)) {
                            selected = false;
                        }
                    }
                }
            }
        }

        private void handleResizingObject() {
            if(resizing) {
                if(resizingVer) {
                    h = AdvancedMath.distance(resizingMiddleY, mouseY) - 32;
                } else {
                    w = AdvancedMath.distance(resizingMiddleX, mouseX) - 32;
                }
            } else if(e.getInput().isButtonDown(1)) {
                if (AdvancedMath.inRadius(mouseX, mouseY,
                        xLeftSide, yLeftSide, 16) ||
                        AdvancedMath.inRadius(mouseX, mouseY,
                                xRightSide, yRightSide, 16)) {
                    // Horizontal stretching
                    resizing = true;
                    resizingVer = true;
                    resizingStartSize = w;
                    resizingStartDistance = AdvancedMath.distance(x, mouseX);
                    resizingMiddleX = xx;
                    resizingMiddleY = yy;
                } else if (AdvancedMath.inRadius(mouseX, mouseY,
                        xTopSide, yTopSide, 16) ||
                        AdvancedMath.inRadius(mouseX, mouseY,
                                xBottomSide, yBottomSide, 16)) {
                    // Horizontal stretching
                    resizing = true;
                    resizingVer = false;
                    resizingStartSize = h;
                    resizingStartDistance = AdvancedMath.distance(y, mouseY);
                    resizingMiddleX = xx;
                    resizingMiddleY = yy;
                }
            }

            if(e.getInput().isButtonUp(1)) {
                resizing = false;
            }
        }

        private void handleMoveAnchorWithMouse() {
            if(true) return;

            if(movingAnchor) {
                int disX = mouseX-anchorX;
                int disY = mouseY-anchorY;

                alignX = mouseX - x - anchorX;
                alignY = mouseY - y - anchorY;
            } else if(e.getInput().isButtonDown(1)) {
                if(AdvancedMath.inRadius(mouseX, mouseY, x, y, 16)) {
                    movingAnchor = true;
                    anchorX = mouseX-x;
                    anchorY = mouseY-y;
                    oldX = x-alignX;
                    oldY = y-alignY;
                }
            }

            if(e.getInput().isButtonUp(1)) {
                movingAnchor = false;
            }
        }

        @Override
        public void render(Core e) {
            AffineTransform previous = g2.getTransform();

            g2.setTransform(AffineTransform.getRotateInstance(Math.toRadians(angle),
                    x-r.getCamX(),
                    y-r.getCamY()));

            g2.drawImage(image, x-r.getCamX()-alignX, y-r.getCamY()-alignY, w, h, null);

            if(selected) {
                r.drawCircle(x, y, 17, Color.black);
                r.drawCircle(x, y, 16, Color.black);
                r.drawCircle(x, y, 15, Color.white);
                r.drawCircle(x, y, 14, Color.white);
            }

            g2.setTransform(previous);

            if(selected) {
                if(!dragging && !rotating && !resizing) {
                    Stroke old = g2.getStroke();
                    g2.setStroke(new BasicStroke(4));
                    r.drawPolygon(area, Color.black);
                    g2.setStroke(old);
                }
                switch(objectState) {
                    case OBJ_STATE_ROTATE:
                        r.drawCircle(x, y, radius, Color.black);
                        int cos, sin;
                        if(rotating) {
                            cos = lengthDir_x(radius/2, angle);
                            sin = lengthDir_y(radius/2, angle);
                        } else {
                            cos = this.cos;
                            sin = this.sin;
                        }
                        r.fillCircle(x + cos, y + sin, 12, Color.red);
                        r.drawCircle(x + cos, y + sin, 12, Color.black);
                        break;
                    case OBJ_STATE_RESIZE:
                        r.fillCircle(xLeftSide, yLeftSide, 16, Color.black);
                        r.fillCircle(xTopSide, yTopSide, 16, Color.black);
                        r.fillCircle(xRightSide, yRightSide, 16, Color.black);
                        r.fillCircle(xBottomSide, yBottomSide, 16, Color.black);
                        break;

                }

                r.fillCircle(xRightSide, yRightSide, 16, Color.yellow);
                r.fillCircle(xBottomSide, yBottomSide, 16, Color.yellow);
            }
        }

        @Override
        public String shareSend() {
            JSONObject object = new JSONObject();
            try {
                object.put("x", x);
                object.put("y", y);
                object.put("w", w);
                object.put("h", h);
                object.put("alignX", alignX);
                object.put("alignY", alignY);
                object.put("depth", depth);
                object.put("angle", angle);
                object.put("imageFile", imageFile);
                object.put("parameters",parameters);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return object.toString().replace("\\", "");
        }

        @Override
        public void shareReceive(String data) {
            try {
                JSONObject object = new JSONObject(data);
                x = object.getInt("x");
                y = object.getInt("y");
                w = object.getInt("w");
                h = object.getInt("h");
                alignX = object.getInt("alignX");
                alignY = object.getInt("alignY");
                depth = object.getInt("depth");
                angle = object.getInt("angle");
                parameters = new ArrayList<>();
                JSONArray array = object.getJSONArray("parameters");
                if(array != null && array.length() > 0) {
                    for(int i = 0; i < array.length(); i++) {
                        parameters.add(array.getString(i));
                    }
                }
                imageFile = object.getString("imageFile");
                image = getImage(imageFile);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }


        // Getters and setters
        public int getX() {
            return x;
        }
        public void setX(int x) {
            this.x = x;
        }
        public int getY() {
            return y;
        }
        public void setY(int y) {
            this.y = y;
        }
        public int getW() {
            return w;
        }
        public void setW(int w) {
            this.w = w;
        }
        public int getH() {
            return h;
        }
        public void setH(int h) {
            this.h = h;
        }
        public int getAlignX() {
            return alignX;
        }
        public void setAlignX(int alignX) {
            this.alignX = alignX;
        }
        public int getAlignY() {
            return alignY;
        }
        public void setAlignY(int alignY) {
            this.alignY = alignY;
        }
        public int getDepth() {
            return depth;
        }
        public void setDepth(int depth) {
            this.depth = depth;
        }
    }

    private static int lengthDir_x(int length, int dir) {
        return (int)(Math.cos(Math.toRadians(dir))*(double)length);
    }

    private static int lengthDir_y(int length, int dir) {
        return (int)(Math.sin(Math.toRadians(dir))*(double)length);
    }
}
