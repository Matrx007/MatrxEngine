package com.engine.libs.networking;

import com.engine.libs.exceptions.PrintError;
import com.engine.libs.input.Input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class MMOClient {
    private Socket client;
    private DataListener dataListener;
    private BufferedReader bufferedReader;
    public MMOClient() {}

    public void initClient(String ip, int port) {
        try {
            client = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataListener = null;
    }

    public void sendData(String data) throws NoConnectionException {
        if(client != null) {
            try {
                client.getOutputStream().write(data.getBytes());
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            } catch (SocketException e) {
                throw new NoConnectionException();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getData() throws NoConnectionException {
        if(client != null) {
            byte[] arr = new byte[1000];
            try {
                client.getInputStream().read(arr);
            } catch (SocketException e) {
                throw new NoConnectionException();
            } catch (IOException e) {
                throw new NoConnectionException(e.getMessage());
            }
            return new String(arr, Charset.defaultCharset()).trim();
        }

        return "$null";
    }

    public String getData(int dataSize) throws NoConnectionException {
        if(client != null) {
            byte[] arr = new byte[dataSize];
            try {
                client.getInputStream().read(arr);
            } catch (SocketException e) {
                throw new NoConnectionException();
            } catch (IOException e) {
                throw new NoConnectionException(e.getMessage());
            }
            return new String(arr, Charset.defaultCharset()).trim();
        }

        return "$null";
    }

    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDataListener(DataListener listener) {

    }

    public void sendKeys(Input input) throws NoConnectionException {
        StringBuilder data = new StringBuilder();
        for(int id = 0; id < input.NUM_KEYS; id++) {
            data.append(String.valueOf(input.keys[id] ? 1 : 0));
        }
        sendData("$keys"+data);
    }

    public void sendKeysLast(Input input) throws NoConnectionException {
        StringBuilder data = new StringBuilder();
        for(int id = 0; id < input.NUM_KEYS; id++) {
            data.append(String.valueOf(input.keysLast[id] ? 1 : 0));
        }
        sendData("$keysLast"+data);
    }

    public void sendMousePress(Input input) throws NoConnectionException {
        StringBuilder data = new StringBuilder();
        for(int id = 0; id < input.NUM_BUTTONS; id++) {
            data.append(String.valueOf(input.buttons[id] ? 1 : 0));
        }
        sendData("$mousePress"+data);
    }

    public void sendMousePressLast(Input input) throws NoConnectionException {
        StringBuilder data = new StringBuilder();
        for(int id = 0; id < input.NUM_BUTTONS; id++) {
            data.append(String.valueOf(input.buttonsLast[id] ? 1 : 0));
        }
        sendData("$mousePressLast"+data);
    }

    public void sendMouseLocation(Input input) throws NoConnectionException {
        sendData("$mouseLocation"+String.valueOf(input.getMouseX()+"-"+String.valueOf(input.getMouseY())));
    }

    public void sendMouseScroll(Input input) throws NoConnectionException {
        sendData("$mouseScroll"+String.valueOf(input.getScroll()));
    }

    public DataListener getDataListener() {
        return dataListener;
    }

    public abstract class DataListener {
        public abstract void processData(String data);
    }
}

