package com.bomberman.models.boosters;

public class Door extends Booster {
    public Door(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.DOOR;
    }
}