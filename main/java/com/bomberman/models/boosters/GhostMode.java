package com.bomberman.models.boosters;

public class GhostMode extends Booster {
    public GhostMode(int x, int y) {
        super(x, y);
    }

    @Override
    protected BoosterType getBoosterSubType() {
        return BoosterType.GHOST_MODE;
    }
}