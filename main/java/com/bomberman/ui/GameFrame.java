package com.bomberman.ui;

import com.bomberman.constants.GameConstants;
import com.bomberman.game.Game;
import com.bomberman.models.Bomberman;
import com.bomberman.models.GameObject;
import com.bomberman.models.terrain.Stone;
import com.bomberman.models.terrain.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameFrame extends JFrame implements KeyListener {
    private Game game;
    private MapPanel mapPanel;
    private StatePanel statePanel;
    private Bomberman player;

    public GameFrame() {
        setTitle("Bomberman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize game with actual content
        game = new Game(15, 15);  // Smaller for testing
        game.initialize();

        // Create and add player
        player = new Bomberman(GameConstants.CELL_SIZE, GameConstants.CELL_SIZE, "Player1");
        game.getPlayers().add(player);

        // Create UI panels
        statePanel = new StatePanel();
        mapPanel = new MapPanel(game, player);

        add(statePanel, BorderLayout.NORTH);
        add(mapPanel, BorderLayout.CENTER);

        // Set focus for keyboard input
        setFocusable(true);
        addKeyListener(this);

        pack();
        setLocationRelativeTo(null);

        // Start game update loop
        Timer gameTimer = new Timer(100, e -> {
            mapPanel.repaint();
            statePanel.updatePlayer(player);
        });
        gameTimer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int step = GameConstants.CELL_SIZE;
        int newX = player.getX();
        int newY = player.getY();

        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                newY -= step;
                break;
            case KeyEvent.VK_DOWN:
                newY += step;
                break;
            case KeyEvent.VK_LEFT:
                newX -= step;
                break;
            case KeyEvent.VK_RIGHT:
                newX += step;
                break;
            case KeyEvent.VK_B:
                int gridX = player.getX() / GameConstants.CELL_SIZE;
                int gridY = player.getY() / GameConstants.CELL_SIZE;
                game.placeBomb(player, gridX, gridY, 2);
                return;
        }

        // Check boundaries
        if (newX >= 0 && newX < (game.getWidth() - 1) * GameConstants.CELL_SIZE &&
                newY >= 0 && newY < (game.getHeight() - 1) * GameConstants.CELL_SIZE) {

            // Check collision with walls and stones
            boolean blocked = false;
            for (GameObject obj : game.getGameObjects()) {
                if (obj instanceof Stone || obj instanceof Wall) {
                    int objX = obj.getX() * GameConstants.CELL_SIZE;
                    int objY = obj.getY() * GameConstants.CELL_SIZE;
                    if (newX == objX && newY == objY) {
                        blocked = true;
                        break;
                    }
                }
            }

            if (!blocked) {
                player.setX(newX);
                player.setY(newY);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}