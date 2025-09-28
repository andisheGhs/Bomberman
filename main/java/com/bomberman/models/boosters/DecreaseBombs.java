package com.bomberman.models.boosters;

public class DecreaseBombs extends Booster {
    public DecreaseBombs(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.DECREASE_BOMBS;
    }
}