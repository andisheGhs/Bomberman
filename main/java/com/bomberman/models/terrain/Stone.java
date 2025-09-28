package com.bomberman.models.terrain;

import com.bomberman.models.GameObject;

public class Stone extends GameObject {

    public Stone(int x, int y) {
        super(x, y);
    }

    @Override
    protected GameObjectType getObjectType() {
        return GameObjectType.STONE;
    }
}