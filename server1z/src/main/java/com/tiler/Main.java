package com.tiler;
public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        Server server = new Server(config);
        server.start();
    }
}
