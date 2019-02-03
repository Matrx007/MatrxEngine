package com.engine.libs.game.behaviors;

import com.engine.libs.game.Mask;
import com.engine.libs.game.GameObject;
import com.engine.libs.math.BasicMath;
import com.engine.libs.math.Point;
import com.engine.libs.world.CollisionMap;

import java.util.ArrayList;

public class AABBCollisionManager {
    private GameObject obj;
    private CollisionMap world;
    public static int MAX_UNSTUCK_TRIES = 16;

    public AABBCollisionManager(GameObject obj, CollisionMap world) {
        this.obj = obj;
        this.world = world;
    }

    public void unstuck() {
        int k = MAX_UNSTUCK_TRIES;
        ArrayList<Mask> collisions = new ArrayList<>();
        while (k > 0 && world.collisionWithExcept(obj.mask, obj.aabbComponent)) {
            k--;

            if (!(obj.mask instanceof Mask.Rectangle)) return;

            collisions.addAll(world.collisionWithWhoExcept(
                    obj.mask, obj.aabbComponent));

            int smallestX = Integer.MAX_VALUE;
            int smallestY = Integer.MAX_VALUE;
            int largestX = Integer.MIN_VALUE;
            int largestY = Integer.MIN_VALUE;

            for (int i = collisions.size() - 1; i >= 0; i--) {
                Mask mask = collisions.get(i);
                if (mask instanceof Mask.Rectangle) {
                    smallestX = Math.min(smallestX, (int) mask.x);
                    smallestY = Math.min(smallestY, (int) mask.y);
                    largestX = Math.max(largestX,
                            (int) mask.x + ((Mask.Rectangle) mask).w);
                    largestY = Math.max(largestY,
                            (int) mask.y + ((Mask.Rectangle) mask).h);
                }
            }

            int currentX = (int) obj.mask.x+((Mask.Rectangle) obj.mask).w/2;
            int currentY = (int) obj.mask.y+((Mask.Rectangle) obj.mask).h/2;

            int distanceToLeftBorder = (currentX - smallestX);// + ((Mask.Rectangle) obj.mask).w;
            int distanceToRightBorder = (largestX - currentX);// + ((Mask.Rectangle) obj.mask).w;
            int distanceToTopBorder = (currentY - smallestY)+1;// + ((Mask.Rectangle) obj.mask).h;
            int distanceToBottomBorder = (largestY - currentY)+1;// + ((Mask.Rectangle) obj.mask).h;

            int nearestBorderX = (distanceToLeftBorder == distanceToRightBorder) ?
                    -distanceToLeftBorder :
                    (distanceToLeftBorder < distanceToRightBorder) ?
                            -distanceToLeftBorder : distanceToRightBorder;
            int nearestBorderY = (distanceToTopBorder == distanceToBottomBorder) ?
                    -distanceToTopBorder :
                    (distanceToTopBorder < distanceToBottomBorder) ?
                            -distanceToTopBorder : distanceToBottomBorder;
            nearestBorderX += Math.signum(nearestBorderX) * ((Mask.Rectangle) obj.mask).w/2;
            nearestBorderY += Math.signum(nearestBorderY) * ((Mask.Rectangle) obj.mask).h/2;
            if (Math.abs(nearestBorderX) < Math.abs(nearestBorderY)) {
                obj.mask.move(nearestBorderX, 0);
                obj.x += nearestBorderX;
            } else if (Math.abs(nearestBorderX) > Math.abs(nearestBorderY)) {
                obj.mask.move(0, nearestBorderY);
                obj.y += nearestBorderY;
            } else {
                obj.mask.move(distanceToRightBorder, distanceToBottomBorder);
                obj.x += distanceToRightBorder;
                obj.y += distanceToBottomBorder;
            }
        }
    }

    public void move(double speedX, double speedY) {
        if(speedX != 0) {
            if (world.collisionWithExcept(obj.mask.shift(speedX, 0), obj.aabbComponent)) {
                int tries = MAX_UNSTUCK_TRIES;
                while (!world.collisionWithExcept(obj.mask.shift((int) BasicMath.sign(speedX)*(MAX_UNSTUCK_TRIES-tries), 0), obj.aabbComponent) &&
                        tries > 0) {
                    obj.mask.move((int) Math.signum(speedX)*(MAX_UNSTUCK_TRIES-tries), 0);
//                obj.mask.x+=(int)BasicMath.sign(speedX);
                    obj.x += BasicMath.sign(speedX)*(MAX_UNSTUCK_TRIES-tries);
                    tries--;
                }
//                obj.mask.move(-(int) Math.signum(speedX), 0);
//                obj.x-= Math.signum(speedX);
                speedX = 0;
            } else {
                obj.mask.move(speedX, 0);
                obj.x += speedX;
            }
        }

        if(speedY != 0) {
            if (world.collisionWithExcept(obj.mask.shift(0, speedY), obj.aabbComponent)) {
                int tries = MAX_UNSTUCK_TRIES;
                while (!world.collisionWithExcept(obj.mask.shift(0, (int) BasicMath.sign(speedY)*(MAX_UNSTUCK_TRIES-tries)), obj.aabbComponent) &&
                        tries > 0) {
//                obj.mask.y+=BasicMath.sign(speedY);
                    obj.mask.move(0, (int) Math.signum(speedY)*(MAX_UNSTUCK_TRIES-tries));
                    obj.y += BasicMath.sign(speedY)*(MAX_UNSTUCK_TRIES-tries);
                    tries--;
                }
//                obj.mask.move(0, -(int) Math.signum(speedY));
//                obj.y-= Math.signum(speedY);

                speedY = 0;
            } else {
                obj.mask.move(0, speedY);
                obj.y += speedY;
            }
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
