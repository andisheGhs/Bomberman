package com.bomberman.models.boosters;

public class DecreaseSpeed extends Booster {
    public DecreaseSpeed(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.DECREASE_SPEED;
    }
}