package com.tiler;
import java.io.*;
import java.util.Properties;

public class Config {
    private static final String fileName = "D:\\Users\\molni\\IdeaProjects\\settings.txt";
    private  int port;
    private  String host;
    public Config(){
        loadSetting();
    }
    private void loadSetting() {
        try {
            File settingsFile = new File(fileName);
            if (settingsFile.exists()) {
                Properties prop = new Properties();
                prop.load(new FileInputStream(fileName));
                port = Integer.parseInt(prop.getProperty("port"));
                host = prop.getProperty("host");
            } else {
                System.err.println("Файл настроек не найден " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
