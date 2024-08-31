package com.tiler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static final String messFile = "D:\\Users\\molni\\IdeaProjects\\messFile.txt";
    private final Config config;
    private final Set<ClientHandler> clients;

    public Server(Config config) {
        this.config = config;
        clients = new HashSet<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(config.getPort())) {
            System.out.println("Чат сервера стартовал, проснулшивается:  " + config.getHost() + ":" + config.getPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новый клиент подключился: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(this, clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
        logMessage(message, sender.getUsername());
    }

    private void logMessage(String message, String username) {
        try {
            String logEntry = formatLogEntry(message, username);
            Files.write(Paths.get(messFile), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatLogEntry(String message, String username) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s: %s%n", now.format(formatter), username, message);
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Клиент отключился");
    }
}
