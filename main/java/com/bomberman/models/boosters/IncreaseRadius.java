package com.bomberman.models.boosters;

public class IncreaseRadius extends Booster {
    public IncreaseRadius(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.INCREASE_RADIUS;
    }
}