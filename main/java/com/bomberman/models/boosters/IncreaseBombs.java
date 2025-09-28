package com.bomberman.models.boosters;

public class IncreaseBombs extends Booster {
    public IncreaseBombs(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.INCREASE_BOMBS;
    }
}