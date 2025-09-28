package com.bomberman.models.enemies;

import java.awt.Point;

public class Enemy3 extends Enemy {

    public Enemy3(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initialize() {
        this.speed = 2;
        this.enemyType = 3;
        this.startLevel = 3;
        this.canPassWalls = false;
        this.canPassStones = false;
    }

    @Override
    public void updateMovement(Point bombermanPosition) {
        if (bombermanPosition == null) {
            currentDirection = getRandomDirection().toPoint();
            return;
        }

        // A* pathfinding would go here for smarter movement
        // For now, use greedy approach
        int dx = bombermanPosition.x - this.x;
        int dy = bombermanPosition.y - this.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            currentDirection = new Point(Integer.signum(dx) * 2, 0);
        } else {
            currentDirection = new Point(0, Integer.signum(dy) * 2);
        }
    }
}