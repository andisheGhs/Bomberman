package com.bomberman.models;

import java.io.Serializable;

public abstract class GameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int x;
    protected int y;
    protected GameObjectType type;

    public enum GameObjectType {
        BOMBERMAN, BOMB, STONE, WALL, GRASS, ENEMY, BOOSTER
    }

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = getObjectType();
    }

    protected abstract GameObjectType getObjectType();

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public GameObjectType getType() { return type; }
}