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

    public static class Rectangle extends Mask {
        public int w, h;
        public String type = "Rectangle";
//        private java.awt.Rectangle.Double rectangle;

        public Rectangle(double x, double y, int w, int h) {
            this.w = w;
            this.h = h;
            this.x = x;
            this.y = y;
//            this.rectangle = new java.awt.Rectangle.Double(x, y, w, h);
        }

        public Rectangle(int x, int y, int w, int h) {
            this.w = w;
            this.h = h;
            this.x = x;
            this.y = y;
//            this.rectangle = new java.awt.Rectangle.Double(x, y, w, h);
        }

        @Override
        public boolean isColliding(int x, int y) {
            return AdvancedMath.inRange(x, y, this.x, this.y, w, h);
//            return new java.awt.Rectangle(x, y, w, h).contains(point.x, point.y);
        }

        private static boolean intersects(double Ax, double Ay, double Aw, double Ah,
                                          double Bx, double By, double Bw, double Bh) {
            return Bx + Bw > Ax &&
                    By + Bh > Ay &&
                    Ax + Aw > Bx &&
                    Ay + Ah > By;
        }

        public boolean isOverlapping(Mask.Rectangle other) {
            if (y < other.y+h
                    || y+h > other.y) {
                return false;
            }
            return !(x + w < other.x)
                    && !(x > other.x + other.w);
        }

        public boolean isColliding(Mask mask) {
            if(mask instanceof Mask.Rectangle) {
                return isOverlapping((Rectangle) mask);

//                return intersects(x, y, w, h, mask.x, mask.y,
//                        ((Rectangle) mask).w, ((Rectangle) mask).h);

//                Mask.Rectangle rectangle = (Mask.Rectangle) mask;
//                return this.rectangle.intersects(new java.awt.Rectangle.Double(rectangle.x, rectangle.y, rectangle.w, rectangle.h));
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

        public String toString() {
            return "x"+String.valueOf(x)+"y"+String.valueOf(y)+"w"+w+"h"+String.valueOf(h);
        }
    }

    /*public static class Line extends Mask {
        public int x1, y1, x2, y2;
        public String type = "Line";

        private double length;

        public Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.length = AdvancedMath.distance(x1, y1, x2, y2);
        }

        @Override
        public boolean isColliding(Point point) {
            return AdvancedMath.distance(x1, y1, point.x, point.y) +
                    AdvancedMath.distance(point.x, point.y, x2, y2) == length;
        }

        public boolean isColliding(Mask mask) {
            if(mask instanceof Mask.Rectangle) {
                Mask.Rectangle rectangle = (Mask.Rectangle) mask;
                return new java.awt.Rectangle(x, y, w, h).intersects(new java.awt.Rectangle(rectangle.x, rectangle.y, rectangle.w, rectangle.h));
            } else {
                PrintError.printMessage("Warning: Unknown shape");
                return false;
            }
        }

        public String toString() {
            return "x"+String.valueOf(x)+"y"+String.valueOf(y)+"w"+w+"h"+String.valueOf(h);
        }
    }*/
}
