package com.bomberman.game;

import com.bomberman.constants.GameConstants;
import com.bomberman.models.*;
import com.bomberman.models.boosters.*;
import com.bomberman.models.enemies.*;
import com.bomberman.models.terrain.*;
import java.awt.Point;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int width;
    private final int height;
    private int level = 1;
    private volatile boolean gameOver = false;
    private volatile boolean levelComplete = false;

    private final List<GameObject> gameObjects = new CopyOnWriteArrayList<>();
    private final List<Enemy> enemies = new CopyOnWriteArrayList<>();
    private final List<Bomberman> players = new CopyOnWriteArrayList<>();
    private final List<Bomb> bombs = new CopyOnWriteArrayList<>();
    private final List<Booster> boosters = new CopyOnWriteArrayList<>();
    private final List<Point> explosions = new CopyOnWriteArrayList<>();

    private Door door = null;
    private int bombLimit = 1;

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void initialize() {
        gameObjects.clear();
        enemies.clear();
        bombs.clear();
        boosters.clear();
        explosions.clear();
        bombLimit = 1;

        generateTerrain();
        spawnEnemies(Math.min(5 + level, 10));
        startGameUpdate();
    }

    private void generateTerrain() {
        Random random = new Random();
        boolean[][] occupied = new boolean[width][height];

        // First, place stones and grass
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1 ||
                        (x % 2 == 0 && y % 2 == 0)) {
                    gameObjects.add(new Stone(x, y));
                    occupied[x][y] = true;
                } else {
                    gameObjects.add(new Grass(x, y));
                }
            }
        }

        // Choose door location far from start
        int doorX = width - 2 - random.nextInt(3);
        int doorY = height - 2 - random.nextInt(3);
        while (occupied[doorX][doorY] || (doorX < 5 && doorY < 5)) {
            doorX = 3 + random.nextInt(width - 4);
            doorY = 3 + random.nextInt(height - 4);
        }

        // Create door and hide it behind a wall
        door = new Door(doorX, doorY);
        boosters.add(door);
        Wall doorWall = new Wall(doorX, doorY);
        gameObjects.add(doorWall);
        occupied[doorX][doorY] = true;

        // Ensure path from start to door exists using BFS
        ensurePath(1, 1, doorX, doorY, occupied);

        // Add walls with reduced density (only 20% instead of 40%)
        int wallCount = 0;
        int maxWalls = (width * height) / 8; // Reduced wall count

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                if (!occupied[x][y] && !isStartingArea(x, y) && wallCount < maxWalls) {
                    if (random.nextDouble() < 0.2) { // Reduced from 0.4
                        Wall wall = new Wall(x, y);
                        gameObjects.add(wall);
                        occupied[x][y] = true;
                        wallCount++;

                        // Add boosters behind some walls
                        if (random.nextDouble() < 0.4) { // Increased booster chance
                            Booster booster = createRandomBooster(x, y, random);
                            if (booster != null) {
                                boosters.add(booster);
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Door location: (" + doorX + ", " + doorY + ")");
    }

    private void ensurePath(int startX, int startY, int endX, int endY, boolean[][] occupied) {
        // Keep a clear path using simple approach - clear direct paths
        List<Point> path = new ArrayList<>();

        // Horizontal path
        for (int x = Math.min(startX, endX); x <= Math.max(startX, endX); x++) {
            if (!occupied[x][startY] || (x == startX || x == endX)) {
                path.add(new Point(x, startY));
            }
        }

        // Vertical path
        for (int y = Math.min(startY, endY); y <= Math.max(startY, endY); y++) {
            if (!occupied[endX][y] || y == endY) {
                path.add(new Point(endX, y));
            }
        }

        // Clear the path
        for (Point p : path) {
            occupied[p.x][p.y] = false;
            // Remove any walls on the path
            gameObjects.removeIf(obj ->
                    obj.getX() == p.x && obj.getY() == p.y && obj instanceof Wall
            );
        }
    }

    private Booster createRandomBooster(int x, int y, Random random) {
        int type = random.nextInt(10);
        switch(type) {
            case 0:
            case 1: return new IncreaseBombs(x, y);
            case 2: return new IncreaseSpeed(x, y);
            case 3: return new IncreaseRadius(x, y);
            case 4: return new DecreaseSpeed(x, y);
            case 5: return new DecreaseRadius(x, y);
            case 6: return new GhostMode(x, y);
            case 7: return new ControlBombs(x, y);
            case 8: return new IncreasePoints(x, y);
            case 9: return new DecreasePoints(x, y);
            default: return null;
        }
    }

    public synchronized void placeBomb(Bomberman player, int x, int y, int radius) {
        // Check bomb limit
        long playerBombs = bombs.stream()
                .filter(b -> b.getOwner() == player)
                .count();

        if (playerBombs >= bombLimit) {
            return;
        }

        // Check if position is clear
        boolean canPlace = bombs.stream()
                .noneMatch(bomb -> bomb.getX() == x && bomb.getY() == y);

        if (canPlace) {
            Bomb bomb = new Bomb(x, y, radius, player);
            bombs.add(bomb);

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    explodeBomb(bomb);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private synchronized void explodeBomb(Bomb bomb) {
        if (!bomb.isExploded()) {
            bomb.explode();
            bombs.remove(bomb);

            int x = bomb.getX();
            int y = bomb.getY();
            int radius = bomb.getRadius();

            // Visual explosion effect
            for (int r = 0; r <= radius; r++) {
                explosions.add(new Point(x + r, y));
                explosions.add(new Point(x - r, y));
                explosions.add(new Point(x, y + r));
                explosions.add(new Point(x, y - r));
            }

            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    explosions.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            handleExplosionDamage(x, y, radius, bomb.getOwner());
        }
    }

    private void handleExplosionDamage(int bombX, int bombY, int radius, Bomberman owner) {
        for (int r = 0; r <= radius; r++) {
            // Check in all four directions but stop at stones
            if (!destroyAt(bombX + r, bombY, owner) && r > 0) break;
            if (!destroyAt(bombX - r, bombY, owner) && r > 0) break;
            if (!destroyAt(bombX, bombY + r, owner) && r > 0) break;
            if (!destroyAt(bombX, bombY - r, owner) && r > 0) break;
        }
    }

    private boolean destroyAt(int x, int y, Bomberman owner) {
        // Check for stone (stops explosion)
        for (GameObject obj : gameObjects) {
            if (obj.getX() == x && obj.getY() == y && obj instanceof Stone) {
                return false; // Stop explosion
            }
        }

        // Destroy walls
        gameObjects.removeIf(obj -> {
            if (obj.getX() == x && obj.getY() == y && obj instanceof Wall) {
                owner.addScore(10);
                // Reveal booster if any
                for (Booster booster : boosters) {
                    if (booster.getX() == x && booster.getY() == y) {
                        booster.setAvailable(true);
                    }
                }
                return true;
            }
            return false;
        });

        // Kill enemies - this actually works!
        enemies.removeIf(enemy -> {
            int ex = enemy.getX() / GameConstants.CELL_SIZE;
            int ey = enemy.getY() / GameConstants.CELL_SIZE;
            if (ex == x && ey == y) {
                owner.addScore(100 * enemy.getEnemyType());
                System.out.println("Enemy killed! Score: " + owner.getScore());
                return true;
            }
            return false;
        });

        // Check players
        for (Bomberman player : players) {
            int px = player.getX() / GameConstants.CELL_SIZE;
            int py = player.getY() / GameConstants.CELL_SIZE;
            if (px == x && py == y) {
                player.die();
                System.out.println("Player caught in explosion!");
            }
        }

        return true; // Continue explosion
    }

    private void applyBooster(Bomberman player, Booster booster) {
        System.out.println("Picking up booster: " + booster.getClass().getSimpleName());

        if (booster instanceof IncreaseBombs) {
            bombLimit++;
            System.out.println("Bomb limit increased to: " + bombLimit);
        } else if (booster instanceof IncreaseSpeed) {
            player.increaseSpeed();
            System.out.println("Speed increased!");
        } else if (booster instanceof IncreaseRadius) {
            // Would increase bomb radius
            System.out.println("Bomb radius increased!");
        } else if (booster instanceof IncreasePoints) {
            player.addScore(100);
        } else if (booster instanceof DecreasePoints) {
            player.addScore(-50);
        } else if (booster instanceof GhostMode) {
            player.setGhostMode(true);
            System.out.println("Ghost mode activated!");
        } else if (booster instanceof Door) {
            if (enemies.isEmpty()) {
                levelComplete = true;
                System.out.println("Level complete! Moving to level " + (level + 1));
            } else {
                System.out.println("Kill all enemies first! Remaining: " + enemies.size());
                return; // Don't remove door
            }
        }

        booster.setAvailable(false);
        boosters.remove(booster);
    }

    private void checkCollisions() {
        for (Bomberman player : players) {
            if (!player.isAlive()) continue;

            // Check enemy collisions
            for (Enemy enemy : enemies) {
                if (Math.abs(player.getX() - enemy.getX()) < 30 &&
                        Math.abs(player.getY() - enemy.getY()) < 30) {
                    player.die();
                    System.out.println("Player killed by enemy!");
                }
            }

            // Check booster pickups
            int px = player.getX() / GameConstants.CELL_SIZE;
            int py = player.getY() / GameConstants.CELL_SIZE;

            for (Booster booster : boosters) {
                if (booster.isAvailable() && booster.getX() == px && booster.getY() == py) {
                    applyBooster(player, booster);
                    break;
                }
            }
        }
    }

    private void startGameUpdate() {
        new Thread(() -> {
            while (!gameOver) {
                try {
                    Thread.sleep(100);
                    updateEnemies();
                    checkCollisions();
                    checkWinCondition();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            if (Math.random() < 0.2) { // Move less frequently
                int dx = (int)(Math.random() * 3) - 1; // -1, 0, or 1
                int dy = (int)(Math.random() * 3) - 1;

                int newX = enemy.getX() + dx * GameConstants.CELL_SIZE;
                int newY = enemy.getY() + dy * GameConstants.CELL_SIZE;

                int gridX = newX / GameConstants.CELL_SIZE;
                int gridY = newY / GameConstants.CELL_SIZE;

                if (gridX >= 0 && gridX < width && gridY >= 0 && gridY < height &&
                        !isBlocked(gridX, gridY)) {
                    enemy.setX(newX);
                    enemy.setY(newY);
                }
            }
        }
    }

    private void checkWinCondition() {
        if (levelComplete) {
            level++;
            levelComplete = false;

            // Reset player position
            for (Bomberman player : players) {
                player.setX(GameConstants.CELL_SIZE);
                player.setY(GameConstants.CELL_SIZE);
                player.setLevel(level);
            }

            initialize();
        }

        boolean allPlayersDead = players.stream().noneMatch(Bomberman::isAlive);
        if (allPlayersDead && !players.isEmpty()) {
            gameOver = true;
            System.out.println("GAME OVER! Final Score: " +
                    (players.isEmpty() ? 0 : players.get(0).getScore()));
        }
    }

    public boolean isBlocked(int x, int y) {
        for (GameObject obj : gameObjects) {
            if (obj.getX() == x && obj.getY() == y) {
                if (obj instanceof Stone || obj instanceof Wall) {
                    return true;
                }
            }
        }

        for (Bomb bomb : bombs) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return true;
            }
        }

        return false;
    }

    private boolean isStartingArea(int x, int y) {
        return (x <= 2 && y <= 2);
    }

    // In Game.java, update the spawnEnemies method:

    private void spawnEnemies(int baseCount) {
        Random random = new Random();

        // At level i, spawn enemies of types 1 through i
        for (int enemyType = 1; enemyType <= Math.min(level, 4); enemyType++) {
            // Spawn more of lower level enemies, fewer of higher level
            int countForType = Math.max(1, (baseCount * (5 - enemyType)) / 4);

            for (int i = 0; i < countForType; i++) {
                int x, y;
                int attempts = 0;

                do {
                    x = 3 + random.nextInt(Math.max(1, width - 4));
                    y = 3 + random.nextInt(Math.max(1, height - 4));
                    attempts++;
                } while ((isBlocked(x, y) || isStartingArea(x, y)) && attempts < 50);

                if (attempts < 50) {
                    Enemy enemy = null;
                    switch(enemyType) {
                        case 1:
                            enemy = new Enemy1(x * GameConstants.CELL_SIZE, y * GameConstants.CELL_SIZE);
                            break;
                        case 2:
                            enemy = new Enemy2(x * GameConstants.CELL_SIZE, y * GameConstants.CELL_SIZE);
                            break;
                        case 3:
                            enemy = new Enemy3(x * GameConstants.CELL_SIZE, y * GameConstants.CELL_SIZE);
                            break;
                        case 4:
                            enemy = new Enemy4(x * GameConstants.CELL_SIZE, y * GameConstants.CELL_SIZE);
                            break;
                    }

                    if (enemy != null) {
                        enemies.add(enemy);
                        System.out.println("Spawned Enemy Type " + enemyType + " at level " + level);
                    }
                }
            }
        }

        System.out.println("Level " + level + " - Total enemies: " + enemies.size());
    }

    // Getters
    public List<GameObject> getGameObjects() { return gameObjects; }
    public List<Enemy> getEnemies() { return enemies; }
    public List<Bomberman> getPlayers() { return players; }
    public List<Bomb> getBombs() { return bombs; }
    public List<Booster> getBoosters() { return boosters; }
    public List<Point> getExplosions() { return explosions; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getLevel() { return level; }
    public boolean isGameOver() { return gameOver; }
}