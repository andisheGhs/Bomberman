package com.bomberman.ui;

import com.bomberman.constants.GameConstants;
import com.bomberman.game.Game;
import com.bomberman.models.*;
import com.bomberman.models.boosters.*;
import com.bomberman.models.enemies.Enemy;
import com.bomberman.models.terrain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MapPanel extends JPanel {
    private Game game;
    private Bomberman localPlayer;
    private Map<String, BufferedImage> images = new HashMap<>();

    public MapPanel(Game game, Bomberman player) {
        this.game = game;
        this.localPlayer = player;
        setPreferredSize(new Dimension(
                game.getWidth() * GameConstants.CELL_SIZE,
                game.getHeight() * GameConstants.CELL_SIZE
        ));
        setBackground(Color.BLACK);
        loadImages();
    }

    private void loadImages() {
        String[] imageFiles = {
                "bomberman.png",
                "bomb_PNG33.png",
                "bomb.png",  // Try both names
                "stone1.jpg",
                "stone.jpg",  // Alternative name
                "grass5.jpg",
                "grass.jpg",
                "wood-wall.jpg",
                "wall.jpg",
                "enemy1.png",
                "enemy2.png",
                "enemy3.png",
                "enemy4.png",
                "deadBomberman.png",
                "dead.png",
                "increaseBombLimit.jpg",
                "increaseBombs.png",
                "decreaseBombLimit.png",
                "decreaseBombs.png",
                "increaseSpeed.jpg",
                "increaseSpeed.png",
                "decreaseSpeed.png",
                "increaseRadius.jpg",
                "increaseRadius.png",
                "decreaseRadius.jpg",
                "decreaseRadius.png",
                "increasePoints.jpg",
                "increasePoints.png",
                "decreasePoints.jpg",
                "decreasePoints.png",
                "controlBombs.jpg",
                "controlBombs.png",
                "ghostBooster.png",
                "ghost.png",
                "levelUp.png",
                "door.png"
        };

        for (String filename : imageFiles) {
            try {
                // Load from resources using classloader
                InputStream stream = getClass().getClassLoader().getResourceAsStream("images/" + filename);
                if (stream != null) {
                    BufferedImage img = ImageIO.read(stream);
                    images.put(filename, img);
                    System.out.println("Successfully loaded: " + filename);
                    stream.close();
                }
            } catch (IOException e) {
                // Silent fail for alternative names
            }
        }

        System.out.println("Total images loaded: " + images.size());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw terrain first
        for (GameObject obj : game.getGameObjects()) {
            drawGameObject(g2, obj);
        }

        // Draw boosters and door
        for (Booster booster : game.getBoosters()) {
            if (booster.isAvailable()) {
                drawBooster(g2, booster);
            }
        }

        // Draw enemies
        for (Enemy enemy : game.getEnemies()) {
            drawEnemy(g2, enemy);
        }

        // Draw bombs
        for (Bomb bomb : game.getBombs()) {
            drawBomb(g2, bomb);
        }

        // Draw explosions
        for (Point p : game.getExplosions()) {
            g2.setColor(new Color(255, 100, 0, 180));
            g2.fillRect(p.x * GameConstants.CELL_SIZE, p.y * GameConstants.CELL_SIZE,
                    GameConstants.CELL_SIZE, GameConstants.CELL_SIZE);
        }

        // Draw players
        for (Bomberman player : game.getPlayers()) {
            drawPlayer(g2, player);
        }

        // Draw game info
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Level: " + game.getLevel(), 10, 20);
        g2.drawString("Enemies: " + game.getEnemies().size(), 100, 20);
        if (game.getEnemies().isEmpty()) {
            g2.setColor(Color.YELLOW);
            g2.drawString("FIND THE DOOR!", 200, 20);
        }
    }

    private void drawGameObject(Graphics2D g, GameObject obj) {
        int x = obj.getX() * GameConstants.CELL_SIZE;
        int y = obj.getY() * GameConstants.CELL_SIZE;
        int size = GameConstants.CELL_SIZE;

        BufferedImage img = null;
        if (obj instanceof Stone) {
            img = images.get("stone1.jpg");
            if (img == null) img = images.get("stone.jpg");
        } else if (obj instanceof Wall) {
            img = images.get("wood-wall.jpg");
            if (img == null) img = images.get("wall.jpg");
        } else if (obj instanceof Grass) {
            img = images.get("grass5.jpg");
            if (img == null) img = images.get("grass.jpg");
        }

        if (img != null) {
            g.drawImage(img, x, y, size, size, null);
        } else {
            // Fallback rendering
            if (obj instanceof Stone) {
                g.setColor(Color.GRAY);
                g.fillRect(x, y, size, size);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, size-1, size-1);
            } else if (obj instanceof Wall) {
                g.setColor(new Color(139, 69, 19));
                g.fillRect(x, y, size, size);
                g.setColor(Color.BLACK);
                g.draw3DRect(x, y, size-1, size-1, true);
            } else if (obj instanceof Grass) {
                g.setColor(new Color(50, 150, 50));
                g.fillRect(x, y, size, size);
            }
        }
    }

    private void drawBooster(Graphics2D g, Booster booster) {
        int x = booster.getX() * GameConstants.CELL_SIZE;
        int y = booster.getY() * GameConstants.CELL_SIZE;
        int size = GameConstants.CELL_SIZE;

        // Draw grass background
        BufferedImage grassImg = images.get("grass5.jpg");
        if (grassImg == null) grassImg = images.get("grass.jpg");
        if (grassImg != null) {
            g.drawImage(grassImg, x, y, size, size, null);
        } else {
            g.setColor(new Color(50, 150, 50));
            g.fillRect(x, y, size, size);
        }

        // Draw the booster
        BufferedImage img = null;
        if (booster instanceof Door) {
            img = images.get("levelUp.png");
            if (img == null) img = images.get("door.png");

            // Always draw door visibly even without image
            if (img == null) {
                g.setColor(new Color(0, 0, 255, 200));
                g.fillRect(x + 5, y + 5, size - 10, size - 10);
                g.setColor(Color.YELLOW);
                g.fillOval(x + size - 20, y + size/2 - 3, 6, 6);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 14));
                g.drawString("EXIT", x + 8, y + 28);
            }
        } else if (booster instanceof IncreaseBombs) {
            img = images.get("increaseBombLimit.jpg");
            if (img == null) img = images.get("increaseBombs.png");
        } else if (booster instanceof DecreaseBombs) {
            img = images.get("decreaseBombLimit.png");
            if (img == null) img = images.get("decreaseBombs.png");
        } else if (booster instanceof IncreaseSpeed) {
            img = images.get("increaseSpeed.jpg");
            if (img == null) img = images.get("increaseSpeed.png");
        } else if (booster instanceof DecreaseSpeed) {
            img = images.get("decreaseSpeed.png");
        } else if (booster instanceof IncreaseRadius) {
            img = images.get("increaseRadius.jpg");
            if (img == null) img = images.get("increaseRadius.png");
        } else if (booster instanceof DecreaseRadius) {
            img = images.get("decreaseRadius.jpg");
            if (img == null) img = images.get("decreaseRadius.png");
        } else if (booster instanceof GhostMode) {
            img = images.get("ghostBooster.png");
            if (img == null) img = images.get("ghost.png");
        } else if (booster instanceof ControlBombs) {
            img = images.get("controlBombs.jpg");
            if (img == null) img = images.get("controlBombs.png");
        }

        if (img != null) {
            g.drawImage(img, x, y, size, size, null);
        } else if (!(booster instanceof Door)) {
            // Fallback for other boosters
            g.setColor(new Color(100, 200, 255, 200));
            g.fillOval(x + 10, y + 10, size - 20, size - 20);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("?", x + 20, y + 32);
        }
    }

    private void drawEnemy(Graphics2D g, Enemy enemy) {
        String imageName = "enemy" + enemy.getEnemyType() + ".png";
        BufferedImage img = images.get(imageName);

        if (img != null) {
            g.drawImage(img, enemy.getX(), enemy.getY(),
                    GameConstants.CELL_SIZE, GameConstants.CELL_SIZE, null);
        } else {
            // Fallback with distinct colors for each enemy type
            Color color;
            switch(enemy.getEnemyType()) {
                case 1:
                    color = Color.GREEN;
                    break;
                case 2:
                    color = Color.BLUE;
                    break;
                case 3:
                    color = Color.RED;
                    break;
                case 4:
                    color = new Color(128, 0, 128); // Purple
                    break;
                default:
                    color = Color.ORANGE;
            }

            g.setColor(color);
            g.fillOval(enemy.getX() + 5, enemy.getY() + 5,
                    GameConstants.CELL_SIZE - 10, GameConstants.CELL_SIZE - 10);

            // Draw enemy type number
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(String.valueOf(enemy.getEnemyType()),
                    enemy.getX() + 20, enemy.getY() + 32);
        }
    }

    private void drawBomb(Graphics2D g, Bomb bomb) {
        BufferedImage img = images.get("bomb_PNG33.png");
        if (img == null) img = images.get("bomb.png");

        int x = bomb.getX() * GameConstants.CELL_SIZE;
        int y = bomb.getY() * GameConstants.CELL_SIZE;

        // Pulsing effect
        double pulse = 1.0 + 0.2 * Math.sin(System.currentTimeMillis() / 100.0);
        int size = (int)(GameConstants.CELL_SIZE * 0.8 * pulse);
        int offset = (GameConstants.CELL_SIZE - size) / 2;

        if (img != null) {
            g.drawImage(img, x + offset, y + offset, size, size, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillOval(x + offset, y + offset, size, size);
            g.setColor(Color.RED);
            g.fillRect(x + 22, y + 10, 6, 10);
            g.setColor(Color.ORANGE);
            g.fillOval(x + 23, y + 8, 4, 4);
        }
    }

    private void drawPlayer(Graphics2D g, Bomberman player) {
        BufferedImage img = player.isAlive() ?
                images.get("bomberman.png") :
                (images.get("deadBomberman.png") != null ? images.get("deadBomberman.png") : images.get("dead.png"));

        if (img != null) {
            g.drawImage(img, player.getX(), player.getY(),
                    GameConstants.CELL_SIZE, GameConstants.CELL_SIZE, null);
        } else {
            // Fallback player rendering
            g.setColor(player.isAlive() ? Color.YELLOW : Color.GRAY);
            g.fillOval(player.getX() + 5, player.getY() + 5,
                    GameConstants.CELL_SIZE - 10, GameConstants.CELL_SIZE - 10);

            if (player.isAlive()) {
                // Draw face
                g.setColor(Color.BLACK);
                g.fillOval(player.getX() + 15, player.getY() + 15, 5, 5);
                g.fillOval(player.getX() + 30, player.getY() + 15, 5, 5);
                g.drawArc(player.getX() + 15, player.getY() + 25, 20, 10, 0, -180);
            }
        }

        // Draw player name
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(player.getName(), player.getX(), player.getY() - 2);
    }
}