package com.engine.libs.networking;

import com.engine.libs.exceptions.PrintError;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.HashMap;

public class MMOServer {
    private ServerSocket server;
    private HashMap<Integer, Socket> serverClients;
    private DataListener dataListener;
    private int timeout = 10, lastJoined;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int clients() {
        return serverClients.size();
    }

    public Socket returnClient(int client) {
        return serverClients.get(client);
    }

    public HashMap<Integer, Socket> getClientList() {
        return serverClients;
    }

    public boolean isOccupied(int client) {
        return serverClients.get(client) != null;
    }

    public void echo() throws NoConnectionException {
        for(int i = 0;i < serverClients.size(); i++) {
            String data = getData(i);

            for(int j = 0; j < serverClients.size(); j++) {
                sendData(data, j);
            }
        }
    }

    public void getClient(int client) {
        //serverClients.remove(client);
        Thread findClient = new Thread(() -> {
            try {
                serverClients.put(client, server.accept());
                lastJoined = client;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        findClient.start();
    }

    public void waitForClient(int client) {
        //serverClients.remove(client);
        try {
            serverClients.put(client, server.accept());
            lastJoined = client;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getClient(int client, Runnable event) {

        //serverClients.remove(client);
        new Thread(() -> {
            try {
                serverClients.put(client, server.accept());
                lastJoined = client;
                event.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void removeClient(int client) {
        serverClients.remove(client);
    }

    public void initServer(int port, int maxClients) {
        serverClients = new HashMap<>();
        serverClients.put(0, null);
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            return;
        }
        /*for(int i = 0; i < maxClients; i++) {
            try {
                serverClients.add(server.accept());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        dataListener = null;
    }

    public void sendData(String data, int clientId) throws NoConnectionException {
        if(serverClients.get(clientId) != null) {
            try {
                serverClients.get(clientId).getOutputStream().write(data.getBytes());
            } catch (SocketException e) {
                throw new NoConnectionException();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getData(int clientId) throws NoConnectionException {
        if(serverClients.get(clientId) != null) {
            byte[] arr = new byte[1000];
            try {
                serverClients.get(clientId).getInputStream().read(arr);
            } catch (SocketException e) {
                throw new NoConnectionException();
            } catch (IOException e) {
                throw new NoConnectionException(e.getMessage());
            }
            return new String(arr, Charset.defaultCharset()).trim();
        }

        return "$null";
    }

    public String getData(int clientId, int dataSize) throws NoConnectionException {
        if(serverClients.get(clientId) != null) {
            byte[] arr = new byte[dataSize];
            try {
                serverClients.get(clientId).getInputStream().read(arr);
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
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < serverClients.size(); i++) {
            try {
                serverClients.get(i).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDataListener(DataListener listener) {

    }

    public DataListener getDataListener() {
        return dataListener;
    }

    public int getLastJoined() {
        return lastJoined;
    }

    public abstract class DataListener {
        public abstract void processData(String data);
    }
}
