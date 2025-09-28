package com.bomberman.models.boosters;

public class ControlBombs extends Booster {
    public ControlBombs(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.CONTROL_BOMBS;
    }
}