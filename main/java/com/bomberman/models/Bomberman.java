package com.bomberman.models;



import com.bomberman.constants.GameConstants;
import java.awt.Point;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class Bomberman extends com.bomberman.models.GameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int speed = GameConstants.DEFAULT_PLAYER_SPEED;
    private volatile boolean alive = true;
    private int score = GameConstants.INITIAL_SCORE;
    private int level = 1;
    private boolean ghostMode = false;
    private boolean bombControl = false;
    private final List<Bomb> bombs = new CopyOnWriteArrayList<>();

    public Bomberman(int x, int y) {
        super(x, y);
    }

    public Bomberman(int x, int y, String name) {
        super(x, y);
        this.name = name;
    }

    @Override
    protected GameObjectType getObjectType() {
        return GameObjectType.BOMBERMAN;
    }

    public synchronized void move(Point direction) {
        this.x += direction.x * GameConstants.MOVEMENT_STEP;
        this.y += direction.y * GameConstants.MOVEMENT_STEP;
    }

    public synchronized void increaseSpeed() {
        if (speed < 5) speed++;
    }

    public synchronized void decreaseSpeed() {
        if (speed > 1) speed--;
    }

    public synchronized void addScore(int points) {
        score += points;
    }

    public synchronized void die() {
        alive = false;
    }

    // Thread-safe getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public synchronized int getSpeed() { return speed; }
    public synchronized void setSpeed(int speed) { this.speed = Math.max(1, Math.min(5, speed)); }

    public synchronized boolean isAlive() { return alive && score >= 0; }
    public synchronized void setAlive(boolean alive) { this.alive = alive; }

    public synchronized int getScore() { return score; }
    public synchronized void setScore(int score) { this.score = score; }

    public synchronized int getLevel() { return level; }
    public synchronized void setLevel(int level) { this.level = level; }

    public synchronized boolean isGhostMode() { return ghostMode; }
    public synchronized void setGhostMode(boolean ghostMode) { this.ghostMode = ghostMode; }

    public synchronized boolean hasBombControl() { return bombControl; }
    public synchronized void setBombControl(boolean bombControl) { this.bombControl = bombControl; }

    public List<Bomb> getBombs() { return bombs; }
}