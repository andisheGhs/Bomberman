package com.bomberman.ui;

import com.bomberman.models.Bomberman;
import javax.swing.*;
import java.awt.*;

public class StatePanel extends JPanel {
    private JLabel timeLabel;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JLabel livesLabel;
    private JLabel bombsLabel;

    private int time = 0;
    private Timer timer;

    public StatePanel() {
        setPreferredSize(new Dimension(1000, 60));
        setBackground(new Color(50, 50, 50));
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        initializeComponents();
        startTimer();
    }

    private void initializeComponents() {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color textColor = Color.WHITE;

        timeLabel = createLabel("Time: 0", labelFont, textColor);
        scoreLabel = createLabel("Score: 100", labelFont, textColor);
        levelLabel = createLabel("Level: 1", labelFont, textColor);
        livesLabel = createLabel("Lives: 3", labelFont, textColor);
        bombsLabel = createLabel("Bombs: 1", labelFont, textColor);

        add(timeLabel);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(scoreLabel);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(levelLabel);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(livesLabel);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(bombsLabel);
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            time++;
            updateTime(time);
        });
        timer.start();
    }

    public void updatePlayer(Bomberman player) {
        if (player != null) {
            SwingUtilities.invokeLater(() -> {
                scoreLabel.setText("Score: " + player.getScore());
                levelLabel.setText("Level: " + player.getLevel());
                livesLabel.setText("Lives: " + (player.isAlive() ? "1" : "0"));
            });
        }
    }

    public void updateTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timeLabel.setText(String.format("Time: %02d:%02d", minutes, secs));
    }

    public void updateBombs(int count) {
        bombsLabel.setText("Bombs: " + count);
    }

    public void reset() {
        time = 0;
        updateTime(0);
    }
}