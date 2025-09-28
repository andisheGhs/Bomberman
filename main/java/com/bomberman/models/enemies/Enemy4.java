package com.bomberman.models.enemies;

import java.awt.Point;

public class Enemy4 extends Enemy {

    public Enemy4(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initialize() {
        this.speed = 2;
        this.enemyType = 4;
        this.startLevel = 4;
        this.canPassWalls = true;
        this.canPassStones = true;
    }

    @Override
    public void updateMovement(Point bombermanPosition) {
        if (bombermanPosition == null) {
            currentDirection = getRandomDirection().toPoint();
            return;
        }

        // Direct path to player (can go through walls)
        int dx = bombermanPosition.x - this.x;
        int dy = bombermanPosition.y - this.y;

        // Normalize movement
        if (Math.abs(dx) > Math.abs(dy)) {
            currentDirection = new Point(Integer.signum(dx) * 2, 0);
        } else {
            currentDirection = new Point(0, Integer.signum(dy) * 2);
        }
    }
}