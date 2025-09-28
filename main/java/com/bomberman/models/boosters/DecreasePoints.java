package com.bomberman.models.boosters;

public class DecreasePoints extends Booster {
    public DecreasePoints(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.DECREASE_POINTS;
    }
}