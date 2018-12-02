package com.engine.libs.graphics.renderEngine;

public class InvalidDimensionsException extends Exception {
    public InvalidDimensionsException(int x) {
        super("Invalid dimensions: "+String.valueOf(x));
    }
}
