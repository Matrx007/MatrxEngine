package com.engine.libs.networking;

import com.engine.libs.exceptions.PrintError;
import com.engine.libs.game.GameObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

@Deprecated
public class P2PConnection {
    private ServerSocket server;
    private Socket client, serverClient;
    private DataListener dataListener;

    public P2PConnection(Socket socket) {
        client = socket;
    }

    public void initServer(int port) throws Exception {
        server = new ServerSocket(port);
        serverClient = server.accept();
        dataListener = null;
    }

    public void initClient(String ip, int port) throws Exception {
        client = new Socket(ip, port);
    }

    public void sendData(String data) throws IOException {
        if(serverClient != null) {
            serverClient.getOutputStream().write(data.getBytes());
        } else if(client != null) {
            client.getOutputStream().write(data.getBytes());
        }
    }

    public void sendObject(GameObject obj) {
        if(obj != null) {
            try {
                sendData(obj.shareSend());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getObject(GameObject obj) {
        try {
            obj.shareReceive(getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getData() throws Exception {
        if(serverClient != null) {
            byte[] arr = new byte[1000];
            serverClient.getInputStream().read(arr);
            return new String(arr, Charset.defaultCharset()).trim();
        } else if(client != null) {
            byte[] arr = new byte[1000];
            client.getInputStream().read(arr);
            return new String(arr, Charset.defaultCharset()).trim();
        }

        return "$null";
    }

    public void close() throws IOException, NullPointerException {
        client.close();
        server.close();
        serverClient.close();
    }

    public void addDataListener(DataListener listener) {

    }

    public DataListener getDataListener() {
        return dataListener;
    }

    public abstract class DataListener {
        public abstract void processData(String data);
    }
}
