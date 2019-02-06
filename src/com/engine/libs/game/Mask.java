package com.engine.libs.game;

import com.engine.libs.exceptions.PrintError;
import com.engine.libs.math.AdvancedMath;
import com.engine.libs.math.Point;

public abstract class Mask {
    public double x, y;
    public boolean isColliding(Point point) {
        return isColliding(point.x, point.y);
    }
    public abstract boolean isColliding(int x, int y);
    public abstract boolean isColliding(Mask mask);
    public abstract Mask shift(double shiftX, double shiftY);
    public abstract void move(double moveX, double moveY);
    public abstract Mask expand(double amount);

    public static class Rectangle extends Mask {
        public int w, h;
        public String type = "Rectangle";

        public Rectangle(double x, double y, int w, int h) {
            this.w = w;
            this.h = h;
            this.x = x;
            this.y = y;
        }

        public Rectangle(int x, int y, int w, int h) {
            this.w = w;
            this.h = h;
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean isColliding(int x, int y) {
            return AdvancedMath.inRange(x, y, this.x, this.y, w, h);
        }

        private static boolean intersects(double Ax, double Ay, double Aw, double Ah,
                                          double Bx, double By, double Bw, double Bh) {
            return Bx + Bw > Ax &&
                    By + Bh > Ay &&
                    Ax + Aw > Bx &&
                    Ay + Ah > By;
        }

        public boolean isColliding(Mask mask) {
            if(mask instanceof Mask.Rectangle) {
                return intersects(x, y, w, h, mask.x, mask.y,
                        ((Rectangle) mask).w, ((Rectangle) mask).h);
            } else {
                PrintError.printMessage("Warning: Incompatible masks");
                return false;
            }
        }

        @Override
        public Mask shift(double shiftX, double shiftY) {
            return new Mask.Rectangle(x+shiftX, y+shiftY, w, h);
        }

        @Override
        public void move(double moveX, double moveY) {
            this.x += moveX;
            this.y += moveY;
        }

        @Override
        public Mask expand(double amount) {
            return new Mask.Rectangle(x-amount, y-amount, (int)(w+amount*2), (int)(h+amount*2));
        }


        public String toString() {
            return "x"+String.valueOf(x)+"y"+String.valueOf(y)+"w"+w+"h"+String.valueOf(h);
        }
    }
}
