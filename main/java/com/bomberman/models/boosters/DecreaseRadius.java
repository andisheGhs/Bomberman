package com.bomberman.models.boosters;

public class DecreaseRadius extends Booster {
    public DecreaseRadius(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.DECREASE_RADIUS;
    }
}