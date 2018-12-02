package com.engine.libs.math;

import java.util.concurrent.ThreadLocalRandom;

public class BasicMath {

    // TODO: Constants
    public static double PI = 3.14159265359;

    // TODO: Methods

    public static double sinDegree(double degree) {
        return Math.sin(Math.toDegrees(degree));
    }

    public static double sinRadian(double radian) {
        return Math.sin(radian);
    }

    public static double cosDegree(double degree) {
        return Math.cos(Math.toDegrees(degree));
    }

    public static double cosRadian(double radian) {
        return Math.cos(radian);
    }

    public static double tanDegree(double degree) {
        return Math.tan(Math.toDegrees(degree));
    }

    public static double tanRadian(double radian) {
        return Math.tan(radian);
    }

    public static double toDegree(double radian) {
        return Math.toDegrees(radian);
    }

    public static double toRadian(double degree) {
        return Math.toRadians(degree);
    }

    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    public static double floor(double a) {
        return Math.floor(a);
    }

    public static double ceil(double a) {
        return Math.ceil(a);
    }

    public static double atan2(double x, double y) {
        return Math.atan2(x, y);
    }

    public static double pow(double a, double b) {
        return Math.pow(a, b);
    }

    public static double round(double a) {
        return Math.round(a);
    }

    public static int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static double abs(double a) {
        return Math.abs(a);
    }

    public static double max(double... nums) {
        double max = Integer.MIN_VALUE;
        for(double num : nums) {
            max = Math.max(max, num);
        }
        return max;
    }

    public static double min(double... nums) {
        double min = Integer.MAX_VALUE;
        for(double num : nums) {
            min = Math.min(min, num);
        }
        return min;
    }

    public static double hypot(double x, double y) {
        return Math.hypot(x, y);
    }

    public static double sign(double a) {
        if(a < 0) {
            return -1;
        } else if(a > 0) {
            return 1;
        } else {
            return 0;
        }
    }

}
