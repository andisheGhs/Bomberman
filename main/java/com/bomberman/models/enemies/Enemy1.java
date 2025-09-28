package com.bomberman.models.enemies;

import java.awt.Point;

public class Enemy1 extends Enemy {

    public Enemy1(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initialize() {
        this.speed = 1;
        this.enemyType = 1;
        this.startLevel = 1;
        this.canPassWalls = false;
        this.canPassStones = false;
    }

    @Override
    public void updateMovement(Point bombermanPosition) {
        // Random movement
        if (currentDirection == null || random.nextInt(10) == 0) {
            Direction dir = getRandomDirection();
            if (dir != null) {
                currentDirection = dir.toPoint();
            }
        }
    }
}