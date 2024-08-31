package com.tiler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String fileName = "D:\\Users\\molni\\IdeaProjects\\settings.txt";

    private int serverPort;

    public Config() {
        loadSettings();
    }

    private void loadSettings() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(fileName));
            serverPort = Integer.parseInt(prop.getProperty("port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getServerPort() {
        return serverPort;
    }
}
