package com.engine.libs.graphics.abstractBody;

import com.engine.libs.rendering.Renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AbstractBody {
    private ArrayList<AbstractPiece> pieces;

    public int x, y, w, h;

    public AbstractBody(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        pieces = new ArrayList<>();
    }
    public AbstractBody(int x, int y, int width, int height, AbstractPiece... pieces) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.pieces = new ArrayList<>();
        this.pieces.addAll(Arrays.asList(pieces));
    }

    public void render(Renderer r) {
        for(AbstractPiece piece : pieces) {
            piece.render(r, x, y, w, h);
        }
    }
}
