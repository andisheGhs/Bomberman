package com.bomberman.models.boosters;

public class IncreasePoints extends Booster {
    public IncreasePoints(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.INCREASE_POINTS;
    }
}