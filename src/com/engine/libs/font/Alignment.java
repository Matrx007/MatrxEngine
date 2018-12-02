package com.engine.libs.font;

public class Alignment {
    public static final int HOR_LEFT = 0;
    public static final int HOR_RIGHT = 1;
    public static final int HOR_CENTER = 2;
    public static final int VER_TOP = 3;
    public static final int VER_MIDDLE = 4;
    public static final int VER_BOTTOM = 5;

    public static final Alignment TOP_LEFT = new Alignment(Alignment.VER_TOP, Alignment.HOR_LEFT);
    public static final Alignment TOP_RIGHT = new Alignment(Alignment.VER_TOP, Alignment.HOR_RIGHT);
    public static final Alignment TOP_CENTER = new Alignment(Alignment.VER_TOP, Alignment.HOR_CENTER);
    public static final Alignment MIDDLE_LEFT = new Alignment(Alignment.VER_MIDDLE, Alignment.HOR_LEFT);
    public static final Alignment MIDDLE_RIGHT = new Alignment(Alignment.VER_MIDDLE, Alignment.HOR_RIGHT);
    public static final Alignment MIDDLE_CENTER = new Alignment(Alignment.VER_MIDDLE, Alignment.HOR_CENTER);
    public static final Alignment BOTTOM_LEFT = new Alignment(Alignment.VER_BOTTOM, Alignment.HOR_LEFT);
    public static final Alignment BOTTOM_RIGHT = new Alignment(Alignment.VER_BOTTOM, Alignment.HOR_RIGHT);
    public static final Alignment BOTTOM_CENTER = new Alignment(Alignment.VER_BOTTOM, Alignment.HOR_CENTER);

    private int alignmenthor, alignmentver;

    public Alignment(int alignmenthor, int alignmentver) {
        this.alignmenthor = alignmenthor;
        this.alignmentver = alignmentver;
    }

    public int getAlignmenthor() {
        return alignmenthor;
    }

    public void setAlignmenthor(int alignmenthor) {
        this.alignmenthor = alignmenthor;
    }

    public int getAlignmentver() {
        return alignmentver;
    }

    public void setAlignmentver(int alignmentver) {
        this.alignmentver = alignmentver;
    }
}
