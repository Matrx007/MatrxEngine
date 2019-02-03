import com.engine.Core;
import com.engine.Game;
import com.engine.libs.game.GameObject;
import com.engine.libs.game.Mask;
import com.engine.libs.game.behaviors.AABBCollisionManager;
import com.engine.libs.game.behaviors.AABBComponent;
import com.engine.libs.graphics.abstractBody.AbstractBody;
import com.engine.libs.graphics.abstractBody.AbstractPiece;
import com.engine.libs.graphics.abstractBody.shapes.Circle;
import com.engine.libs.graphics.abstractBody.shapes.Rectangle;
import com.engine.libs.input.Input;
import com.engine.libs.math.AdvancedMath;
import com.engine.libs.math.BasicMath;
import com.engine.libs.rendering.Renderer;
import com.engine.libs.world.CollisionMap;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.SplittableRandom;

import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_T;

public class EntryPoint extends Game {
    public static void main(String[] args) {
        if(args[1].equalsIgnoreCase("open-gl")) {
            System.setProperty("sun.java2d.opengl","True");
        }

        new EntryPoint();
    }

    private Obj obj;
    private CollisionMap collisionMap;
    private SplittableRandom random;

    public EntryPoint() {
        e.width = 500;
        e.height = 350;
        e.scale = 2f;
        e.start();

        // TODO: Init
        collisionMap = new CollisionMap();
        obj = new Obj(64, 64);

        random = new SplittableRandom();
        for(int i = 0; i < 10; i++) {
            int w = random.nextInt(96)+64;
            int h = random.nextInt(96)+64;
            int x = e.width/4+random.nextInt(e.width/2)-w/2;
            int y = e.height/4+random.nextInt(e.height/2)-h/2;
            collisionMap.add(new AABBComponent(
                    new Mask.Rectangle(x, y, w, h)));
        }
        collisionMap.refresh();

        e.run();
    }

    @Override
    public void update(Core e) {
        obj.update(e.getInput());

        if(e.getInput().isKeyDown(VK_C)) {
            int w = random.nextInt(96)+64;
            int h = random.nextInt(96)+64;
            int x = e.width/4+random.nextInt(e.width/2)-w/2;
            int y = e.height/4+random.nextInt(e.height/2)-h/2;
            collisionMap.add(new AABBComponent(
                    new Mask.Rectangle(x, y, w, h)));
            collisionMap.refresh();
        }

        if(e.getInput().isKeyDown(VK_G)) {
            collisionMap.empty();
        }
    }

    @Override
    public void render(Core e) {
        e.getRenderer().fillRectangle(0, 0, e.width, e.height, Color.gray);
        collisionMap.getComponents().forEach(aabbComponent -> {
            Mask.Rectangle mask = (Mask.Rectangle) aabbComponent.area;
            e.getRenderer().fillRectangle(mask.x, mask.y, mask.w, mask.h, Color.blue);
        });
        obj.render(e.getRenderer());
    }

    private class Obj extends GameObject {

        private Mask.Rectangle mask_;
        private AABBCollisionManager cm;

        public Obj(int x, int y) {
            super(0, 0);
            this.x = x;
            this.y = y;
            mask_ = new Mask.Rectangle(x-24, y-24, 48, 48);
            this.mask = mask_;
            this.cm = new AABBCollisionManager(this, collisionMap);
        }

        @Override
        public void update(Input i) {
            if(i.isButtonDown(MouseEvent.BUTTON1)) {
                this.x = i.getMouseX();
                this.y = i.getMouseY();
                moveMask();
            }
            if(i.isKeyDown(VK_T)) {
                cm.unstuck();
            }
            if(i.isButton(MouseEvent.BUTTON3)) {
                cm.move(i.getMouseX()-x, i.getMouseY()-y);
                /*double angle = AdvancedMath.angle((int)x, (int)y,
                        i.getMouseX(), i.getMouseY())+90;
                cm.move(Math.cos(Math.toRadians(angle))*4d,
                        Math.sin(Math.toRadians(angle))*4d);*/
            }
        }

        @Override
        public void render(Renderer r) {
            r.fillRectangle(mask_.x, mask_.y, mask_.w, mask_.h, Color.red);
        }

        private void moveMask() {
            mask_.x = x-(double)mask_.w/2;
            mask_.y = y-(double)mask_.h/2;
        }

        public String shareSend() {
            return null;
        }
        public void shareReceive(String data) {

        }
    }
}
