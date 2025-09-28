package com.bomberman.models.terrain;

import com.bomberman.models.GameObject;

public class Grass extends GameObject {

    public Grass(int x, int y) {
        super(x, y);
    }

    @Override
    protected GameObjectType getObjectType() {
        return GameObjectType.GRASS;
    }
}