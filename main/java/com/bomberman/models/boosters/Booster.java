package com.bomberman.models.boosters;

import com.bomberman.models.GameObject;
import java.io.Serializable;

public abstract class Booster extends GameObject implements Serializable {
    private static final long serialVersionUID = 1L;
    protected boolean available = false;
    protected BoosterType boosterType;

    public enum BoosterType {
        INCREASE_BOMBS, DECREASE_BOMBS, INCREASE_SPEED, DECREASE_SPEED,
        INCREASE_RADIUS, DECREASE_RADIUS, CONTROL_BOMBS, GHOST_MODE,
        INCREASE_POINTS, DECREASE_POINTS, DOOR
    }

    public Booster(int x, int y) {
        super(x, y);
        this.boosterType = getBoosterSubType();
    }

    @Override
    protected GameObjectType getObjectType() {
        return GameObjectType.BOOSTER;
    }

    protected abstract BoosterType getBoosterSubType();

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public BoosterType getBoosterType() { return boosterType; }
}