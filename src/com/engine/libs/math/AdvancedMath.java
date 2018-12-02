package com.engine.libs.math;

import java.awt.*;

import static com.engine.libs.math.BasicMath.*;

public class AdvancedMath {
    // TODO: Methods

    public static double angle(Point source, Point destination) {
        double xDiff = source.x - destination.x;
        double yDiff = source.y - destination.y;
        return Math.toDegrees(Math.atan2(yDiff, xDiff)) + 90.0F;
    }

    public static double angle(int x1, int y1, int x2, int y2) {
        double xDiff = x1 - x2;
        double yDiff = y1 - y2;
        return Math.toDegrees(Math.atan2(yDiff, xDiff)) + 90.0F;
    }

    public static double distance(int fromx, int fromy, int tox, int toy) {
        return hypot(fromx - tox, fromy - toy);
    }

    public static int distance(int fromx, int tox) {
        return Math.abs(tox-fromx);
    }

    public static double lerp(double point1, double point2, double alpha) {
        return point1 + alpha * (point2 - point1);
    }

    public static boolean inRange(double x, double y, double checkx, double checky, double checkw, double checkh) {
        return x >= checkx && x <= checkx+checkw && y >= checky && y <= checky+checkh;
    }

    public static boolean inRange(double x, double y, Rectangle check) {
        return x > check.x && x < check.x+check.width && y > check.y && y < check.y+check.height;
    }

    public static boolean inRadius(double x, double y, double checkx, double checky, double radius) {
        return distance((int)checkx, (int)checky, (int)x, (int)y) < radius;
    }

    public static double setRange(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double map(double n, double start1, double stop1, double start2, double stop2) {
        try {
            return ((n-start1)/(stop1-start1))*(stop2-start2)+start2;
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
