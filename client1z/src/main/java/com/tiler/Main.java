package com.tiler;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    Config config = new Config();
    Scanner scanner = new Scanner(System.in);
    System.out.print("Введите ваш никнейм: ");
        String username = scanner.nextLine();
        Client client = new Client(config, username);
        client.start();
    }
}
