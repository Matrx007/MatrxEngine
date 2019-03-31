package com.engine.libs.input;

import com.engine.Core;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private Core Core;

    public final int NUM_KEYS = 256;
    public boolean[] keys = new boolean[NUM_KEYS];
    public boolean[] keysLast = new boolean[NUM_KEYS];

    public final int NUM_BUTTONS = 5;
    public boolean[] buttons = new boolean[NUM_BUTTONS];
    public boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    public int lastKeyReleased, lastKeyPressed, lastKey;

    public int mouseX, mouseY;
    public int scroll;
    public int lastChar;

    public boolean holdingShift;

    public Input(Core Core) {
        this.Core = Core;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        Core.getWindow().getCanvas().addKeyListener(this);
        Core.getWindow().getCanvas().addMouseListener(this);
        Core.getWindow().getCanvas().addMouseMotionListener(this);
        Core.getWindow().getCanvas().addMouseWheelListener(this);
    }

    public void addKeyListener(KeyListener keyListener) {
        Core.getWindow().getCanvas().addKeyListener(keyListener);
    }

    public void update() {
        for(int i = 0; i < NUM_KEYS; i++) {
            keysLast[i] = keys[i];
        }

        for(int i = 0; i < NUM_BUTTONS; i++) {
            buttonsLast[i] = buttons[i];
        }

        lastKeyReleased = -1;
        lastKeyPressed = -1;
    }

    public boolean isKey(int keyCode) {
        return keys[keyCode];

    }

    public boolean isKeyUp(int keyCode) {
        return !keys[keyCode] && keysLast[keyCode];
    }

    public boolean isKeyDown(int keyCode) {
        return keys[keyCode] && !keysLast[keyCode];
    }

    public boolean isButton(int button) {
        return buttons[button];

    }

    public boolean isButtonUp(int button) {
        return !buttons[button] && buttonsLast[button];

    }

    public boolean isButtonDown(int button) {
        return buttons[button] && !buttonsLast[button];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyChar();
        if(Character.isLetter(id) || Character.isSpaceChar(id) || Character.isDigit(id)) {
            lastChar = id;
        }
        if(e.getKeyCode() < NUM_KEYS) {
            keys[e.getKeyCode()] = true;
            lastKeyPressed = e.getKeyCode();
            lastKey = e.getKeyCode();
        }

        if(e.isShiftDown()) {
            holdingShift = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() < NUM_KEYS) {
            lastChar = -1;
            keys[e.getKeyCode()] = false;
            lastKeyReleased = e.getKeyCode();
//            lastKey = -1;
        }

        if(!e.isShiftDown()) {
            holdingShift = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int)(e.getX() / Core.getScale());
        mouseY = (int)(e.getY() / Core.getScale());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int)(e.getX() / Core.getScale());
        mouseY = (int)(e.getY() / Core.getScale());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getRelativeMouseX() {
        return mouseX+ Core.getRenderer().getCamX();
    }

    public int getRelativeMouseY() {
        return mouseY+ Core.getRenderer().getCamY();
    }

    public int getScroll() {
        return scroll;
    }

    public boolean[] getKeys() {
        return keys;
    }

    public boolean[] getKeysLast() {
        return keysLast;
    }

    public boolean[] getButtons() {
        return buttons;
    }

    public boolean[] getButtonsLast() {
        return buttonsLast;
    }

    public int getLastKeyReleased() {
        return lastKeyReleased;
    }

    public int getLastKeyPressed() {
        return lastKeyPressed;
    }
}