package com.bomberman.models;

import com.bomberman.constants.GameConstants;
import java.io.Serializable;
import java.util.concurrent.ScheduledFuture;

public class Bomb extends com.bomberman.models.GameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int radius;
    private final Bomberman owner;
    private volatile boolean exploded = false;
    private transient ScheduledFuture<?> bombTimer;

    public Bomb(int x, int y, Bomberman owner) {
        this(x, y, GameConstants.DEFAULT_BOMB_RADIUS, owner);
    }

    public Bomb(int x, int y, int radius, Bomberman owner) {
        super(x, y);
        this.radius = radius;
        this.owner = owner;
    }

    @Override
    protected GameObjectType getObjectType() {
        return GameObjectType.BOMB;
    }

    public synchronized void explode() {
        if (!exploded) {
            exploded = true;
            if (bombTimer != null) {
                bombTimer.cancel(false);
            }
        }
    }

    public int getRadius() { return radius; }
    public Bomberman getOwner() { return owner; }
    public synchronized boolean isExploded() { return exploded; }

    public void setBombTimer(ScheduledFuture<?> timer) {
        this.bombTimer = timer;
    }
}
