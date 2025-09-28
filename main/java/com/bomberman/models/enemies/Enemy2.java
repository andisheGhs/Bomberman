package com.bomberman.models.enemies;

import java.awt.Point;

public class Enemy2 extends Enemy {
    private int moveCounter = 0;

    public Enemy2(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initialize() {
        this.speed = 1;
        this.enemyType = 2;
        this.startLevel = 2;
        this.canPassWalls = false;
        this.canPassStones = false;
    }

    @Override
    public void updateMovement(Point bombermanPosition) {
        moveCounter++;

        // Every 10 moves, try to move toward player
        if (moveCounter % 10 == 0 && bombermanPosition != null) {
            int dx = bombermanPosition.x - this.x;
            int dy = bombermanPosition.y - this.y;

            // Choose horizontal or vertical movement
            if (Math.abs(dx) > Math.abs(dy)) {
                currentDirection = new Point(Integer.signum(dx), 0);
            } else {
                currentDirection = new Point(0, Integer.signum(dy));
            }
        } else if (currentDirection == null || random.nextInt(15) == 0) {
            Direction dir = getRandomDirection();
            if (dir != null) {
                currentDirection = dir.toPoint();
            }
        }
    }
}