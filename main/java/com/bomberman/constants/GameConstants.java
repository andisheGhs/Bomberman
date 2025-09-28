package com.bomberman.constants;

public class GameConstants {
    public static final int CELL_SIZE = 50;
    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 20;
    public static final int DEFAULT_ENEMIES = 10;
    public static final String DEFAULT_SERVER = "localhost";
    public static final int DEFAULT_PORT = 8090;
    public static final int BOMB_TIMER_MS = 5000;
    public static final int DEFAULT_BOMB_RADIUS = 1;
    public static final int DEFAULT_BOMB_LIMIT = 1;
    public static final int DEFAULT_PLAYER_SPEED = 2;
    public static final int MOVEMENT_STEP = 5;
    public static final int INITIAL_SCORE = 100;
    public static final int WALL_DESTROY_SCORE = 1;
    public static final int ENEMY_KILL_SCORE_MULTIPLIER = 2;
    public static final int PLAYER_KILL_SCORE = 10;
    public static final int GAME_UPDATE_INTERVAL_MS = 10;
    public static final int RENDER_INTERVAL_MS = 100;
    public static final int ENEMY_MOVE_INTERVAL_MS = 150;

    private GameConstants() {}
}