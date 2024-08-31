package com.tiler;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Server server;
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            initConnection();
            processMessages();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
    public void initConnection() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        username = in.readLine();
        server.broadcast(username + " присоединился к чату.", this);
    }

    public void processMessages() throws IOException {
        String message;
        while ((message = in.readLine()) != null) {
            if (message.equalsIgnoreCase("/exit")) {
               closeConnection();
               return;
            }
            server.broadcast(username + ": " + message, this);
        }
    }

    public void closeConnection() {
        server.removeClient(this);
        server.broadcast(username + " покинул чат.", this);
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (out != null && !socket.isClosed()) {
            out.println(message);
        }
    }

    public String getUsername() {
        return username;
    }
}
