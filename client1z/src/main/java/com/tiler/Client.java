package com.tiler;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Client {
    private static final String logFile = "D:\\Users\\molni\\IdeaProjects\\file.log";
    private final Config config;
    private final String username;

    public Client(Config config, String username) {
        this.config = config;
        this.username = username;
    }

    public void start() {
        try (Socket socket = new Socket("localhost", config.getServerPort());
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {
            System.out.println("Подключен к чату сервера!");
            writer.println(username);

            new Thread(() -> {
                try {
                    String message;
                    while (!Thread.currentThread().isInterrupted() && reader.ready()) {
                        if ((message = reader.readLine()) != null) {
                            System.out.println(message);
                            logMessage(message);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Сокет закрыт" + e.getMessage());
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        System.err.println("Ошибка закрытия reader " + e.getMessage());
                    }
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("/exit")) {
                    writer.println(message);
                    break;
                } else {
                    writer.println(message);
                    logMessage(username + ": " + message);
                }
            }
            scanner.close();
            writer.close();
            System.out.println("Вы вышли из чата");
        } catch (ConnectException e) {
            System.err.println("Не удалось подключиться. Проверьте сервер или настройки");
        } catch (IOException e) {
            System.err.println("Не удалось подключиться. Проверьте сервер или настройки");
        }
    }
    private void logMessage(String message) {
        try {
            String logEntry = formatLog(message);
            Files.write(Paths.get(logFile), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String formatLog(String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s%n", now.format(formatter), message);
    }
}
