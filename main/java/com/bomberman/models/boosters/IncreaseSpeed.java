package com.bomberman.models.boosters;

public class IncreaseSpeed extends Booster {
    public IncreaseSpeed(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.INCREASE_SPEED;
    }
}