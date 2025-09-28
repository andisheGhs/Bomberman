package com.bomberman.models.terrain;

import com.bomberman.models.GameObject;

public class Wall extends GameObject {

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    protected GameObjectType getObjectType() {
        return GameObjectType.WALL;
    }
}