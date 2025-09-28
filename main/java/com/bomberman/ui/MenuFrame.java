package com.bomberman.ui;

import com.bomberman.network.Client;
import com.bomberman.network.Server;
import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {
    private JButton hostButton;
    private JButton joinButton;
    private JButton exitButton;
    private JTextField nameField;
    private JTextField serverField;
    private JTextField portField;

    public MenuFrame() {
        setTitle("Bomberman - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        layoutComponents();

        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        nameField = new JTextField("Player" + (int)(Math.random() * 1000));
        serverField = new JTextField("localhost");
        portField = new JTextField("8090");

        hostButton = new JButton("Host Game");
        joinButton = new JButton("Join Game");
        exitButton = new JButton("Exit");

        hostButton.addActionListener(e -> hostGame());
        joinButton.addActionListener(e -> joinGame());
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void layoutComponents() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Server:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(serverField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("Port:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(portField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(hostButton);
        buttonPanel.add(joinButton);
        buttonPanel.add(exitButton);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void hostGame() {
        try {
            int port = Integer.parseInt(portField.getText());
            Server server = new Server(port);
            server.start();

            Client client = new Client(serverField.getText(), port, nameField.getText());
            client.connect();

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error starting server: " + e.getMessage());
        }
    }

    private void joinGame() {
        try {
            int port = Integer.parseInt(portField.getText());
            Client client = new Client(serverField.getText(), port, nameField.getText());
            client.connect();

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error connecting: " + e.getMessage());
        }
    }
}