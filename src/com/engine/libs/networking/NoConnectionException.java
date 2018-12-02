package com.engine.libs.networking;

public class NoConnectionException extends Exception {
    public NoConnectionException() { super("Connection broken"); }

    public NoConnectionException(String message) {
        super(message);
    }
}
