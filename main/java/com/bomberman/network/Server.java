//package main.java.com.bomberman.network;

package com.bomberman.network;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        new Thread(() -> {
            System.out.println("Server started on port " + port);
            // Server logic here
        }).start();
    }
}
