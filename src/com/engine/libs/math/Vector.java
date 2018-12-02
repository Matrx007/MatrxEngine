package com.engine.libs.math;

public class Vector {
    public int x, y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void divide(double amount) {
        double x = this.x, y = this.y;

        this.x = (int)(x / amount);
        this.y = (int)(y / amount);
    }

    public void multiply(double amount) {
        double x = this.x, y = this.y;

        this.x = (int)(x * amount);
        this.y = (int)(y * amount);
    }

    public void divide(Vector vector) {
        double x = this.x, y = this.y;

        this.x = (int)(x / vector.x);
        this.y = (int)(y / vector.y);
    }

    public void multiply(Vector vector) {
        double x = this.x, y = this.y;

        this.x = (int)(x * vector.x);
        this.y = (int)(y * vector.y);
    }

    public void add(Vector vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void subtract(Vector vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    public void subtract(int x, int y) {
        this.x -= x;
        this.y -= y;
    }
}
