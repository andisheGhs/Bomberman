package com.bomberman.models.enemies;

import com.bomberman.models.GameObject;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Enemy extends GameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int speed;
    protected int enemyType;
    protected int startLevel;
    protected boolean canPassWalls;
    protected boolean canPassStones;
    protected Point currentDirection;
    protected final Random random = new Random();

    private volatile Point targetPosition;
    private final List<Direction> possibleDirections = new ArrayList<>();

    public enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        public final int dx, dy;
        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public Point toPoint() {
            return new Point(dx, dy);
        }
    }

    public Enemy(int x, int y) {
        super(x, y);
        initialize();
    }

    protected abstract void initialize();

    @Override
    protected GameObjectType getObjectType() {
        return GameObjectType.ENEMY;
    }

    public abstract void updateMovement(Point bombermanPosition);

    public synchronized void move() {
        if (currentDirection != null) {
            x += currentDirection.x * 5;
            y += currentDirection.y * 5;
        }
    }

    public synchronized void updatePossibleDirections(List<Direction> directions) {
        possibleDirections.clear();
        possibleDirections.addAll(directions);
    }

    protected Direction getRandomDirection() {
        if (possibleDirections.isEmpty()) {
            return null;
        }
        return possibleDirections.get(random.nextInt(possibleDirections.size()));
    }

    // Getters and setters
    public int getSpeed() { return speed; }
    public int getEnemyType() { return enemyType; }
    public int getStartLevel() { return startLevel; }
    public boolean canPassWalls() { return canPassWalls; }
    public boolean canPassStones() { return canPassStones; }
}