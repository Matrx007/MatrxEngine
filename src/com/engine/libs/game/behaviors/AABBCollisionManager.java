package com.engine.libs.game.behaviors;

import com.engine.libs.game.Mask;
import com.engine.libs.game.GameObject;
import com.engine.libs.math.BasicMath;
import com.engine.libs.math.Point;
import com.engine.libs.world.CollisionMap;

public class AABBCollisionManager {
    private GameObject obj;
    private CollisionMap world;
    public static int MAX_UNSTUCK_TRIES = 128;

    public AABBCollisionManager(GameObject obj, CollisionMap world) {
        this.obj = obj;
        this.world = world;
    }

    public void move(double speedX, double speedY) {
        if(speedX != 0) {
            if (world.collisionWithExcept(obj.mask.shift(speedX, 0), obj.aabbComponent)) {
                int tries = MAX_UNSTUCK_TRIES;
                while (!world.collisionWithExcept(obj.mask.shift((int) BasicMath.sign(speedX), 0), obj.aabbComponent) &&
                        tries > 0) {
                    obj.mask.move((int) Math.signum(speedX), 0);
//                obj.mask.x+=(int)BasicMath.sign(speedX);
                    obj.x += BasicMath.sign(speedX);
                    tries--;
                }
//                obj.mask.move(-(int) Math.signum(speedX), 0);
//                obj.x-= Math.signum(speedX);
                speedX = 0;
            }
            if (speedX != 0)
                obj.mask.move(speedX, 0);
//        obj.mask.x+=speedX;
            obj.x += speedX;
        }

        if(speedY != 0) {
            if (world.collisionWithExcept(obj.mask.shift(0, speedY), obj.aabbComponent)) {
                int tries = MAX_UNSTUCK_TRIES;
                while (!world.collisionWithExcept(obj.mask.shift(0, (int) BasicMath.sign(speedY)), obj.aabbComponent) &&
                        tries > 0) {
//                obj.mask.y+=BasicMath.sign(speedY);
                    obj.mask.move(0, (int) Math.signum(speedY));
                    obj.y += BasicMath.sign(speedY);
                    tries--;
                }
//                obj.mask.move(0, -(int) Math.signum(speedY));
//                obj.y-= Math.signum(speedY);

                speedY = 0;
            }
            if (speedY != 0)
                obj.mask.move(0, speedY);
//        obj.mask.y+=speedY;
            obj.y += speedY;
        }
    }

    public void moveWithStepHeight(int speedX, int speedY, int stepHeight) {
        if(speedX != 0) {
            if (world.collisionWith(obj.mask.shift(speedX, 0))) {
                int yAdd = 0;
                while (world.collisionWith(obj.mask.shift(
                        speedX, -yAdd)) && yAdd <= stepHeight) {
                    yAdd++;
                }
                if (world.collisionWith(obj.mask.shift(speedX, -yAdd))) {
                    while (!world.collisionWith(obj.mask.shift((int) BasicMath.sign(speedX), 0))) {
                        obj.mask.move((int) Math.signum(speedX), 0);
//                obj.mask.x+=(int)BasicMath.sign(speedX);
                        obj.x += BasicMath.sign(speedX);
                    }
                    speedX = 0;
                } else {
                    obj.mask.move(0, -yAdd);
                    obj.y -= yAdd;
                }
            }
            if (speedX != 0)
                obj.mask.move(speedX, 0);
//        obj.mask.x+=speedX;
            obj.x += speedX;
        }
        if(speedY != 0) {
            if (world.collisionWith(obj.mask.shift(0, speedY))) {
                while (!world.collisionWith(obj.mask.shift(0, (int) BasicMath.sign(speedY)))) {
//                obj.mask.y+=BasicMath.sign(speedY);
                    obj.mask.move(0, (int) Math.signum(speedY));
                    obj.y += BasicMath.sign(speedY);
                }
                speedY = 0;
            }
            if (speedY != 0)
                obj.mask.move(0, speedY);
//        obj.mask.y+=speedY;
            obj.y += speedY;
        }
    }

    /*
    public void move2(int speedX, int speedY) {

        if(world.collisionWith(createMask(speedX, 0))) {
            while(!world.collisionWith(createMask((int)BasicMath.sign(speedX), 0))) {
                obj.mask.x+=(int)BasicMath.sign(speedX);
                obj.x+=BasicMath.sign(speedX);
            }
            speedX = 0;
        }
        obj.mask.x+=speedX;
        obj.x += speedX;

        if(world.collisionWith(createMask(0, speedY))) {
            while(!world.collisionWith(createMask(0, (int)BasicMath.sign(speedY)))) {
                obj.mask.y+=BasicMath.sign(speedY);
                obj.y+=BasicMath.sign(speedY);
            }
            speedY = 0;
        }
        obj.mask.y+=speedY;
        obj.y += speedY;
    }

    public void move(int speedX, int speedY) {
        obj.x += speedX;
        obj.y += speedY;
        Mask.Rectangle maskRectangle;
        if(obj.mask != null) {
            maskRectangle = obj.mask;
        } else {
            return;
        }
        maskRectangle.x += speedX;
        maskRectangle.y += speedY;

        Mask.Rectangle rec = maskRectangle;
        boolean component = world.collisionWith(rec);

        if(component) {
            while (world.collisionWith(rec)) {
                obj.x -= BasicMath.sign(speedX);
                obj.y -= BasicMath.sign(speedY);
                maskRectangle.x -= BasicMath.sign(speedX);
                maskRectangle.y -= BasicMath.sign(speedY);
            }
        }
    }*/
}
