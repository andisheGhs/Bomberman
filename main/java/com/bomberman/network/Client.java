//package main.java.com.bomberman.network;

package com.bomberman.network;

import com.bomberman.ui.GameFrame;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    private String serverAddress;
    private int port;
    private String playerName;
    private Socket socket;

    public Client(String serverAddress, int port, String playerName) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.playerName = playerName;
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, port);
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}